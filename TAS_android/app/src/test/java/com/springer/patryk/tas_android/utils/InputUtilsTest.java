package com.springer.patryk.tas_android.utils;

import android.support.v4.media.MediaMetadataCompat;
import android.text.Editable;
import android.widget.EditText;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Patryk on 11.11.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class InputUtilsTest {

    private static final String FAKE_TEXT = "FakeText";

    @Test
    public void checkIsEmptyReturnFalse() throws Exception {
        assertFalse(InputUtils.checkIsEmpty(FAKE_TEXT));
    }

    @Test
    public void checkIsEmptyReturnTrue() throws Exception {
        assertTrue(InputUtils.checkIsEmpty(""));
        assertTrue(InputUtils.checkIsEmpty("    "));
    }

    @Test
    public void testValidEmail() throws Exception{
        assertTrue(InputUtils.checkIsValidEmail("qweq.dasd@gmail.com"));
    }

    @Test
    public void testInvalidEmail() throws Exception {
        assertFalse(InputUtils.checkIsValidEmail("eqweq@ddsa"));
    }

    @Test
    public void testValidLogin() throws Exception {
        assertTrue(InputUtils.checkIsValidLogin("login"));
    }

    @Test
    public void testInvalidLogin() throws Exception {
        assertFalse(InputUtils.checkIsValidLogin("!dasdwe"));
    }

    @Test
    public void testSamePassword() throws Exception {
        assertTrue(InputUtils.comparePassword("ewqeq", "ewqeq"));
    }

    @Test
    public void testPasswordWithBlankSpace() throws Exception {
        assertTrue(InputUtils.comparePassword(" ewqeq","ewqeq"));
    }

    @Test
    public void testNotSamePasswords() throws Exception {
        assertFalse(InputUtils.comparePassword("password", "passwor"));
    }

    @Test
    public void testEmptyPasswords() throws Exception {
        assertTrue(InputUtils.comparePassword("",""));
    }
}