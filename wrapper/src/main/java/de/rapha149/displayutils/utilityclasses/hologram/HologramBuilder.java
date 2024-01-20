package de.rapha149.displayutils.utilityclasses.hologram;

import de.rapha149.displayutils.utilityclasses.hologram.provider.GeneralHologramContentProvider;
import de.rapha149.displayutils.utilityclasses.hologram.provider.HologramContentProvider;
import de.rapha149.displayutils.utilityclasses.hologram.provider.PlayerHologramContentProvider;
import org.bukkit.Location;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class HologramBuilder {

    private String identifier;
    private List<String> lines;
    private Location loc;
    private HologramContentProvider provider;
    private boolean playerSpecific;
    private Integer updateInterval;
    private List<UUID> players;
    private Supplier<List<UUID>> playersSupplier;

    public HologramBuilder(String identifier, List<String> lines, Location loc) {
        this.identifier = identifier;
        this.lines = lines;
        this.loc = loc;
    }

    public HologramBuilder useGeneralProvider(GeneralHologramContentProvider provider) {
        this.provider = provider;
        this.playerSpecific = false;
        return this;
    }

    public HologramBuilder useSpecificPlayerProvider(PlayerHologramContentProvider provider) {
        this.provider = provider;
        this.playerSpecific = true;
        return this;
    }

    public HologramBuilder withAutomaticUpdates(int updateInterval) {
        this.updateInterval = updateInterval;
        return this;
    }

    public HologramBuilder withPlayers(List<UUID> players) {
        this.players = players;
        return this;
    }

    public HologramBuilder withPlayers(Supplier<List<UUID>> playersSupplier) {
        this.playersSupplier = playersSupplier;
        return this;
    }

    public Hologram build() {
        Objects.requireNonNull(provider, "Provider must be set.");
        return new Hologram(identifier, lines, loc, provider, playerSpecific, updateInterval, players, playersSupplier);
    }
}