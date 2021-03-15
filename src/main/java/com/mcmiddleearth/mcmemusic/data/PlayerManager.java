package com.mcmiddleearth.mcmemusic.data;

import com.mcmiddleearth.mcmemusic.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public class PlayerManager {

    private Set<UUID> deafenedPlayers = new HashSet<>();

    private File deafenedPlayersFile = new File(Main.getInstance().getDataFolder(),"deafenedPlayers.dat");

    public PlayerManager() {
        loadDeafenedPlayers();
    }

    public boolean isDeafened(Player player) {
        return deafenedPlayers.contains(player.getUniqueId());
    }

    public void deafen(Player player) {
        deafenedPlayers.add(player.getUniqueId());
        saveDeafenedPlayers();
    }

    public void undeafen(Player player) {
        deafenedPlayers.remove(player.getUniqueId());
        saveDeafenedPlayers();
    }

    private void loadDeafenedPlayers() {
        if(deafenedPlayersFile.exists()) {
            try (Scanner scanner = new Scanner(deafenedPlayersFile)) {
                while (scanner.hasNext()) {
                    deafenedPlayers.add(UUID.fromString(scanner.nextLine()));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDeafenedPlayers() {
        try(PrintWriter writer = new PrintWriter(new FileWriter(deafenedPlayersFile))) {
            deafenedPlayers.forEach(player -> writer.println(player.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
