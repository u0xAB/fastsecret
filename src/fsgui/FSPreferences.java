/** ************************************************************ */
/* Copyright (c) 2016 Andrej Borovski [andrej.borovski@mail.com]

This file is part of FastSecret.

FastSecret is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/> */
/** ****************************************************************** */
package fsgui;

import java.util.prefs.Preferences;

/**
 *
 * @author Andrej Borovski
 */
public class FSPreferences {
    final Preferences prefs = Preferences.userNodeForPackage(FastSecret.class);
    static final String DEFAULT_STORAGE = "default_storage";
    static final String SPWD_LEN = "spwd_len";
    static final String AUTOLOCK_TIMEOUT = "autolock_timeout";
    public String getDefaultStorage()
    {
        return prefs.get(DEFAULT_STORAGE, "");
    }
    public void setDefaultStorage(String value)
    {
        prefs.put(DEFAULT_STORAGE, value);
    }
    public int getDefaultPasswordLength()
    {
        return prefs.getInt(SPWD_LEN, 12);
    }
    public void setDefaultPasswordLength(int value)
    {
        prefs.putInt(SPWD_LEN, value);
    }
    public int getAutoLockTimeout()
    {
        return prefs.getInt(AUTOLOCK_TIMEOUT, 15);
    }
    public void setAutoLockTimeout(int value)
    {
        prefs.putInt(AUTOLOCK_TIMEOUT, value);
    }
}
