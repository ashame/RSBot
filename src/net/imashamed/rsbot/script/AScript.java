package net.imashamed.rsbot.script;

/*
 *  This file is part of RSBot.
 *
 *  RSBot is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  RSBot is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with RSBot.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.powerbot.script.ClientContext;
import org.powerbot.script.PollingScript;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nathan
 *         created on 8/16/2018.
 */
public abstract class AScript<C extends ClientContext> extends PollingScript<C> {
    public Map<String, Object> scriptVars;

    public AScript() {
        scriptVars = new HashMap<>();
    }
}
