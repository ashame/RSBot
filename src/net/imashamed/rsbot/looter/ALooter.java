package net.imashamed.rsbot.looter;

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

import net.imashamed.rsbot.looter.tasks.*;
import net.imashamed.rsbot.script.AScript;
import net.imashamed.rsbot.script.Task;
import org.powerbot.bot.rt4.client.Tile;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author nathan
 *         created on 8/16/2018.
 */
@Script.Manifest(name="Just a looter", description="but only addy arrows for now :thinking:")
public class ALooter extends AScript<ClientContext> {
    private List<Task> taskList = new ArrayList<>();
    private List<Integer> itemList = new ArrayList<>();

    @Override
    public void start() {
        loadProperties();

        JFrame frame = new JFrame("A looter");

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel mainPanel = new JPanel();
        JButton start = new JButton("Start");

        mainPanel.setLayout(new GridLayout(1, 2));

        start.addActionListener(e -> { //TODO: startup tasks
            if (!ctx.players.local().valid()) {
                JOptionPane.showMessageDialog(frame, "Error setting start tile! (player not valid?)");
                return;
            }
            if (updateProperties() && scriptVars.size() > 0) {
                System.out.println(" success");
                System.out.printf("script variables loaded...\r\n%s\r\n", scriptVars.toString());
                loadTasks();
                frame.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(frame, "error loading script variables...");
            }
        });

        tabbedPane.addTab("Main", mainPanel);

        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        frame.getContentPane().add(start, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(720, 400));
        frame.setResizable(false);
        frame.pack();
    }

    @Override
    public void poll() {
        for (Task t : taskList)
            if (t.activate()) t.execute();
    }

    private boolean loadTasks() {
        taskList.add(new Loot(ctx, scriptVars));
        if (Integer.parseInt(String.valueOf(scriptVars.get("eatFood"))) == 1)
            taskList.add(new Eat(ctx, scriptVars));
        return taskList.size() > 0;
    }

    private boolean loadProperties() {
        Properties prop = new Properties();
        InputStream in;
        try {
            in = new FileInputStream("config.ini");
            prop.load(in);

            List properties = Collections.list(prop.propertyNames());

            for (Object o : properties) {
                scriptVars.put((String) o, prop.get(o));
            }
        } catch (FileNotFoundException e) {
            System.err.print("config file not found... attempting to generate defaults...");
            if (generateDefaultProperties())
                loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean updateProperties() {
        Properties prop = new Properties();
        OutputStream out;
        try {
            out = new FileOutputStream("config.ini");
            System.out.print("updating config with new values...");

            for (String s : scriptVars.keySet()) {
                prop.setProperty(s, (String) scriptVars.get(s));
            }

            prop.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(" failed");
            return false;
        }
        return true;
    }

    private boolean generateDefaultProperties() {
        Properties prop = new Properties();
        OutputStream out = null;
        try {
            out = new FileOutputStream("config.ini");

            prop.setProperty("itemList", "[]");
            prop.setProperty("eatFood", "0");
            prop.setProperty("lootRadius", "8");
            prop.setProperty("startTile", String.valueOf(ctx.players.local().tile()));

            prop.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("success");
        return true;
    }
}
