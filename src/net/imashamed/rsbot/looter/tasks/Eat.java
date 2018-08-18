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
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.Map;

/**
 * @author nathan
 *         created on 8/17/2018.
 */

public class Eat extends Task<ClientContext> {

    public static final int[] FOOD_IDS = {};

    public Eat(ClientContext ctx, Map<String, Object> scriptVars) {
        super(ctx, scriptVars);
    }

    @Override
    public boolean activate() {
        return ctx.players.local().healthPercent() < (int) scriptVars.get("foodThreshold") && !ctx.inventory.select().id(FOOD_IDS).isEmpty();
    }

    @Override
    public void execute() {
        Item food = ctx.inventory.select().id(FOOD_IDS).poll();
        food.click();
        if (Condition.wait(() -> !food.valid(), 100, 50)) {
            System.out.printf("successfully ate %s\r\n", food.name());
        } else {
            System.err.printf("error occurred while eating %s\r\n", food.name());
        }
    }
}
