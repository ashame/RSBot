package net.imashamed.rsbot.looter.tasks;

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

import net.imashamed.rsbot.script.Task;
import org.powerbot.script.rt4.GeItem;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.Map;

/**
 * @author nathan
 *         created on 8/17/2018.
 */

public class Bank extends Task<ClientContext> {
    public Bank(ClientContext ctx, Map<String, Object> scriptVars) {
        super(ctx, scriptVars);
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.size() == 28 && ctx.inventory.select().id(Eat.FOOD_IDS).size() == 0) || inventoryValue() >= (int) scriptVars.get("bankAt");
    }

    @Override
    public void execute() {

    }

    private int inventoryValue() {
        int total = 0;
        for (Item item : ctx.inventory.select()) {
            total += new GeItem(item.id()).price;
        }
        return total;
    }
}
