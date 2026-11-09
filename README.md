# Classic Music

A small client-only NeoForge mod for Minecraft 1.21.1 that brings back the old soundtrack:
only C418's tracks, played by the old (1.13 to 1.15) music rules.

No audio is bundled. The mod's `assets/classicmusic/sounds.json` points at C418 tracks the game already ships.

## What plays where

| Situation | Tracks | Gap between tracks |
|---|---|---|
| Title menu | Mutation, Moog City 2, Beginning 2, Floating Trees | 1 to 30 s |
| Survival / adventure | Minecraft, Clark, Sweden, Subwoofer Lullaby, Living Mice, Haggstrom, Danny, Key, Oxygène, Dry Hands, Wet Hands, Mice on Venus | 10 to 20 min |
| Creative (flying allowed) | Biome Fest, Blind Spots, Haunt Muskie, Aria Math, Dreiton, Taswell + the survival tracks | 1 to 3 min |
| Nether (any biome) | Concrete Halls, Dead Voxel, Warmth, Ballad of the Cats | 1 to 3 min |
| Underwater in an ocean or river | Axolotl, Dragon Fish, Shuniji | 10 to 20 min |
| The End | The End | 5 to 20 min |
| Ender Dragon fight | Boss | immediately |
| End credits | Alpha | immediately |

Old-version behaviour it copies:

- No biome-specific music. Newer composers' tracks (Lena Raine, Kumi Tanioka, Aaron Cherof, Amos Roddy) never play from this mod.
- Changing situation (menu to world, survival to creative, entering the Nether) cuts the current track, then the next one starts within half the new situation's minimum gap.
- Underwater music only starts if a survival track is not already playing, and keeps going until it ends even after you surface.
- Modded dimensions use the overworld rules.
- Screens from other mods that ask for their own music are left alone.

Music discs and the "Music" volume slider work as usual.

## Install

Drop `classicmusic-<version>.jar` into the client's `mods` folder. Servers do not need it.

## Build

```
./gradlew build
```

The jar lands in `build/libs/`. Same toolchain as the robot mod (NeoForge 21.1.265, ModDevGradle, Java 21).
