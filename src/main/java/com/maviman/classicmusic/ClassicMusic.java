package com.maviman.classicmusic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.SelectMusicEvent;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Plays only C418's soundtrack, chosen by the old (1.13 to 1.15) music rules.
 *
 * Each situation uses one of this mod's sound events (assets/classicmusic/sounds.json).
 * Those events point at C418 tracks the game already ships, so no audio is bundled.
 * The events are never registered: the music system only looks them up by name in
 * sounds.json, which keeps the mod client-only and safe on any server.
 */
@Mod(value = ClassicMusic.MOD_ID, dist = Dist.CLIENT)
public final class ClassicMusic {
    public static final String MOD_ID = "classicmusic";

    // Old delays in ticks (20 ticks = 1 s). In old versions every situation change
    // cut the current track, so replaceCurrentMusic is true for all of them.
    static final Music MENU = music("music.menu", 20, 600);
    static final Music GAME = music("music.game", 12000, 24000);
    static final Music CREATIVE = music("music.creative", 1200, 3600);
    static final Music NETHER = music("music.nether", 1200, 3600);
    static final Music UNDER_WATER = music("music.under_water", 12000, 24000);
    static final Music END = music("music.end", 6000, 24000);
    static final Music END_BOSS = music("music.dragon", 0, 0);
    static final Music CREDITS = music("music.credits", 0, 0);

    public ClassicMusic() {
        NeoForge.EVENT_BUS.addListener(ClassicMusic::onSelectMusic);
    }

    private static Music music(String event, int minDelay, int maxDelay) {
        SoundEvent sound = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, event));
        return new Music(Holder.direct(sound), minDelay, maxDelay, true);
    }

    private static void onSelectMusic(SelectMusicEvent event) {
        Music classic = pick(Minecraft.getInstance(), event.getPlayingMusic());
        if (classic != null) {
            event.setMusic(classic);
        }
    }

    /** Old Minecraft#getSituationalMusic. Returns null to leave the game's choice alone. */
    @SuppressWarnings("deprecation") // vanilla uses Abilities.mayfly here too
    static Music pick(Minecraft mc, SoundInstance playing) {
        if (mc.screen instanceof WinScreen) {
            return CREDITS;
        }
        if (mc.screen != null && mc.screen.getBackgroundMusic() != null) {
            // Another mod's screen asked for its own music: respect it.
            return null;
        }
        LocalPlayer player = mc.player;
        if (player == null) {
            return MENU;
        }
        Level level = player.level();
        if (level.dimension() == Level.NETHER) {
            return NETHER;
        }
        if (level.dimension() == Level.END) {
            return mc.gui.getBossOverlay().shouldPlayMusic() ? END_BOSS : END;
        }
        // Underwater music starts only when no survival track is playing, and once it
        // starts it keeps going until the track ends (old behaviour).
        boolean underwater = isPlaying(playing, UNDER_WATER)
                || (player.isUnderWater()
                        && !isPlaying(playing, GAME)
                        && level.getBiome(player.blockPosition()).is(BiomeTags.PLAYS_UNDERWATER_MUSIC));
        if (underwater) {
            return UNDER_WATER;
        }
        return player.getAbilities().instabuild && player.getAbilities().mayfly ? CREATIVE : GAME;
    }

    private static boolean isPlaying(SoundInstance playing, Music music) {
        return playing != null && playing.getLocation().equals(music.getEvent().value().getLocation());
    }
}
