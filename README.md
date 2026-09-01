# Mualla Music

Mualla Music is an Android music player focused on a fast, private, and
customizable listening experience. This repository contains the Android
application and the supporting playback and media modules used to build it.

## Highlights

- Search, browse, and play music with a modern Android interface
- Background playback, queue management, and media controls
- Synced lyrics and multiple lyrics providers
- Playlist, library, artist, and album views
- Optional integrations for Last.fm, Spotify, Discord, and music recognition
- Google Play services and FOSS distributions
- Mobile and Android TV variants
- Universal, ARM64, ARMv7, x86, and x86_64 APK builds

## Build locally

Requirements:

- JDK 21
- Android SDK with API 37
- Git with submodule support

Clone the repository with its required modules:

```bash
git clone --recurse-submodules https://github.com/ThT0AltayHRFls/Mualla-Music.git
cd Mualla-Music
```

Build a debug APK:

```bash
./gradlew assembleGmsMobileUniversalDebug
```

Build a release APK:

```bash
./gradlew assembleGmsMobileUniversalRelease
```

The available release variants are generated from the distribution, device,
and ABI flavor dimensions. The GitHub Actions workflow builds the supported
GMS and FOSS combinations and publishes the resulting artifacts.

## Optional configuration

API credentials and signing values are read from `local.properties` or
environment variables. Do not commit credentials, signing keys, or generated
local configuration files.

## Project structure

- `app` — Mualla Music Android application
- `core` — shared playback and media functionality
- `lyrics` — lyrics provider modules
- `canvas` — artwork and visualizer support
- `lastfm`, `spotifycore`, and `shazamkit` — optional service modules
- `morideobfuscator` and `moriextractor` — media extraction support
- `.github/workflows` — CI, APK packaging, and release automation

## Contributing

Before opening a pull request, build the affected variant and verify that the
corresponding GitHub Actions workflow passes. Keep user credentials and
private signing material out of commits.

## License

See [LICENSE](LICENSE) for the license and attribution information included
with this project.