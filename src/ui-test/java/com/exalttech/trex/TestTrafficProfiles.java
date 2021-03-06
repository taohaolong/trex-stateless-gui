package com.exalttech.trex;

import com.exalttech.trex.ui.models.datastore.Profiles;
import com.exalttech.trex.ui.models.datastore.ProfilesWrapper;
import com.exalttech.trex.util.files.XMLFileManager;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;

import org.apache.commons.io.FileUtils;

import org.junit.Assert;
import org.junit.Test;

import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.exalttech.trex.util.FileChooserFactory;
import com.exalttech.trex.util.files.FileManager;


public class TestTrafficProfiles extends TestBase {
    @Test
    public void testCreateProfile() {
        openTrafficProfilesDialog();

        assertCall(
                () -> {
                    clickOn("#create-profile-button");
                },
                () -> lookup("#profile-stream-name-dialog").query() != null
        );
        setText("#profile-stream-name-dialog-name-text-field", "Test Profile");
        assertCall(
                () -> {
                    clickOn("#profile-stream-name-dialog-ok-button");
                },
                () -> lookup("#profile-stream-name-dialog").query() == null
        );
        final ListView profileList = lookup("#traffic-profile-dialog-profiles-list-view").query();
        Assert.assertTrue(profileList.getItems().contains("Test Profile.yaml"));
    }

    @Test
    public void testLoadProfile() throws Exception {
        openTrafficProfilesDialog();

        final FileChooser fileChooser = Mockito.spy(FileChooser.class);
        FileChooserFactory.set(fileChooser);
        final File file = new File(getTestTrafficProfilesFolder() + "/profile.yaml");
        Mockito.doReturn(file).when(fileChooser).showOpenDialog(Mockito.any());

        assertCall(
                () -> {
                    clickOn("#load-profile-button");
                    FileChooserFactory.set(null);
                },
                () -> {
                    final ListView profileList = lookup("#traffic-profile-dialog-profiles-list-view").query();
                    return profileList.getItems().contains("profile.yaml");
                }
        );
    }

    @Test
    public void testDeleteProfile() throws Exception {
        prepareProfile();
        openTrafficProfilesDialog();

        final ListView profileList = lookup("#traffic-profile-dialog-profiles-list-view").query();
        Assert.assertTrue(profileList.getItems().contains("profile.yaml"));

        assertCall(
                () -> {
                    clickOn("profile.yaml");
                    clickOn("#delete-profile-button");
                    type(KeyCode.ENTER);
                },
                () -> !profileList.getItems().contains("profile.yaml")
        );
    }
    @Test
    public void testExportProfileToJSON() throws Exception {
        prepareProfile();
        openTrafficProfilesDialog();

        final ListView profileList = lookup("#traffic-profile-dialog-profiles-list-view").query();
        Assert.assertTrue(profileList.getItems().contains("profile.yaml"));

        final FileChooser fileChooser = Mockito.spy(FileChooser.class);
        FileChooserFactory.set(fileChooser);
        final File result = new File(FileManager.getLocalFilePath() + "/profile.json");
        Mockito.doReturn(result).when(fileChooser).showSaveDialog(Mockito.any());

        tryCall(
                () -> {
                    clickOn("profile.yaml");
                    clickOn("#export-profile-to-json-button");
                },
                () -> true
        );

        final File expected = new File(getResourcesFolder() + "/profile.json");
        Assert.assertTrue(FileUtils.contentEquals(expected, result));
    }
    @Test
    public void testExportProfileToYAML() throws Exception {
        prepareProfile();
        openTrafficProfilesDialog();

        final ListView profileList = lookup("#traffic-profile-dialog-profiles-list-view").query();
        Assert.assertTrue(profileList.getItems().contains("profile.yaml"));

        final FileChooser fileChooser = Mockito.spy(FileChooser.class);
        FileChooserFactory.set(fileChooser);
        final File result = new File(FileManager.getLocalFilePath() + "/profile.yaml");
        Mockito.doReturn(result).when(fileChooser).showSaveDialog(Mockito.any());

        tryCall(
                () -> {
                    clickOn("profile.yaml");
                    clickOn("#export-profile-to-yaml-button");
                    FileChooserFactory.set(null);
                },
                () -> true
        );

        final File expected = new File(getResourcesFolder() + "/profile.yaml");
        Assert.assertTrue(FileUtils.contentEquals(expected, result));
    }

    private void openTrafficProfilesDialog() {
        assertCall(
                () -> {
                    clickOn("#main-traffic-profiles-menu");
                    clickOn("#main-traffic-profiles-menu-traffic-profiles");
                },
                () -> lookup("#traffic-profile-dialog").query() != null
        );
    }

    private void prepareProfile() throws IOException {
        final File profile = new File(getTestTrafficProfilesFolder() + "/profile.yaml");
        final File profilesDir = new File(FileManager.getProfilesFilePath());
        FileUtils.copyFileToDirectory(profile, profilesDir);

        List<Profiles> profilesList = new ArrayList<>();
        profilesList.add(new Profiles("profile.yaml", profilesDir + "/profile.yaml"));
        ProfilesWrapper wrapper = new ProfilesWrapper(profilesList);
        XMLFileManager.saveXML("profiles.xml", wrapper, ProfilesWrapper.class);
    }
}
