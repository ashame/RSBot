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
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;
import org.powerbot.script.rt4.GroundItem;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @author nathan
 *         created on 8/16/2018.
 */

public class Loot extends Task<ClientContext> {
    private int minValue = -1;
    private final int[] lootIds = {890};
    private final int radius;
    private final Tile startTile;

    public Loot(ClientContext ctx, Map<String, Object> scriptVars) {
        super(ctx, scriptVars);
        this.radius = Integer.parseInt(String.valueOf(scriptVars.get("lootRadius")));
        this.startTile = tileFromString(String.valueOf(scriptVars.get("startTile")));
        if (Integer.parseInt(String.valueOf(scriptVars.get("eatFood"))) == 1)
            this.minValue = Integer.parseInt(String.valueOf(scriptVars.get("minValue")));
    }

    @Override
    public boolean activate() {
        return ctx.inventory.size() < 28 && ctx.players.local().healthPercent() >= 50 && itemsNearby();
    }

    @Override
    public void execute() {
        if (minValue > 0)
            System.out.printf("%s\r\n", lootByValue() ? "success!" : "failed");
        else
            System.out.printf("%s\r\n", lootById() ? "success!" : "failed");
    }

    public boolean itemsNearby() {
        System.out.printf("searching for nearby items... %d found!\r\n", ctx.groundItems.select().id(lootIds).size());
        for (GroundItem item : ctx.groundItems.select().id(lootIds).nearest()) {
            if (item.valid() && startTile.distanceTo(item.tile()) <= radius)
                return true;
        }
        return false;
    }

    public boolean lootById() {
        System.out.printf("attempting to loot %d items by id...\r\n", ctx.groundItems.select().id(lootIds).nearest().size());
        GroundItem item = ctx.groundItems.select().id(lootIds).nearest().peek();
        if (item.valid()) {
            if (!item.inViewport()) {
                ctx.camera.turnTo(item);
                Condition.wait(() -> item.inViewport(), 100, 10);
            }
            item.interact("Take", item.name());
            return Condition.wait(() -> !item.valid(), 100, 30);
        }
        return false;
    }

    public boolean lootByValue() {
        System.out.println("generating list of nearby items...");
        Queue<GroundItem> items = new LinkedList<>();
        for (GroundItem item : ctx.groundItems.nearest()) {
            if (item.valid() && startTile.distanceTo(item.tile()) > radius)
                break;
            GeItem geItem = new GeItem(item.id());
            if (geItem.price > minValue)
                items.add(item);
        }
        System.out.printf("%d items found! attempting to loot...", items.size());
        if (items.peek().valid()) {
            if (!items.peek().inViewport()) {
                ctx.camera.turnTo(items.peek());
                Condition.wait(() -> items.peek().inViewport(), 100, 10);
            }
            items.peek().interact("Take", items.peek().name());
        }
        return Condition.wait(() -> items.peek().valid(), 100, 30);
    }

    public Tile tileFromString(String s) {
        s = s.substring(1, s.length() - 1);
        String[] t = s.split(",");
        if (t.length == 3)
            return new Tile(Integer.parseInt(t[0].trim()), Integer.parseInt(t[1].trim()), Integer.parseInt(t[2].trim()));
        return null;
    }
}
