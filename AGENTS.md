# Custom Dev Instructions for Media Playback

The following rules and requirements are mandatory for any modifications, maintenance, or additions of media features in this application.

## ExoPlayer Media Requirement

"Use Android Media3 ExoPlayer for all media playback. Do NOT use Android MediaPlayer or WebView-based video players."

## Required ExoPlayer Features

### Media3 ExoPlayer
- **MP3 Support**: High-performance audio playback.
- **MP4 Support**: High-fidelity video playback.
- **M3U8 Live Streaming Support**: HLS support for live TV and radio streams.
- **DASH Streaming Support**: Dynamic Adaptive Streaming over HTTP (.mpd).
- **Background Audio Playback**: Background audio service integration.
- **Background Video Playback**: Keep video sound playing or stream background.
- **Audio Focus Management**: Handle ducking, pausing when phone rings or other media plays.
- **Lock Screen Controls**: Standard media commands on lock screen.
- **Media Notification Controls**: Standard play/pause/reset/skip/prev custom notification.
- **Playback Speed Control (0.5x – 3x)**: Native speed modification using `setSpeed` in Player Manager.
- **Subtitle Support**: Text/VTT/SRT integration.
- **Fullscreen Video Player**: Tap to toggle fullscreen view.
- **Auto Resume Playback**: Recover position when app resumes or network restores.
- **Auto Reconnect On Network Loss**: Handle network buffering gracefully with retry logic.
- **Buffering Indicator**: Show clear state during live stream loads or slow connections.
- **Picture-in-Picture (PiP)**: Support for background/PiP video playback on supported devices.
- **Playlist Support**: Seamless transition between media items.
- **Sleep Timer**: Turn off media after designated durations.
- **Media Caching & Offline Playback**: Save network bandwidth.

### Download System
- **Built-in Download Manager**: Manage file downloads cleanly.
- **Audio Download**: Download lectures and audios locally.
- **Video Download**: Save educational content.
- **Quran Audio Download**: Offline audio playback of Quran recitations.
- **Waz Download**: Islamic speech download.
- **Resume Download / Pause Download**: Pause and resume downloads.
- **Download Progress Bar & Notification**: Visible download progress in the UI and notifications bar.
- **Offline Media Library**: Browse and play files saved in local storage.

### Supported Formats
- MP3
- MP4
- M4A
- AAC
- WAV
- FLAC
- OGG
- HLS (.m3u8)
- DASH (.mpd)

### Media Tabs
- Live Makkah TV
- Live Madinah TV
- Live Quran TV
- Live Islamic TV
- Live Islamic Radio
- Video Waz
- Audio Waz
- Quran Recitation
- Nasheed
- Downloaded Media

### Required Dependencies
- `androidx.media3:media3-exoplayer:1.8.0`
- `androidx.media3:media3-ui:1.8.0`
- `androidx.media3:media3-session:1.8.0`

## Final AI Studio Instruction

"All audio, video, live TV, live radio, Quran recitations, waz content and streaming services must use Android Media3 ExoPlayer. Do not use MediaPlayer. Support MP3, MP4, M3U8 and DASH formats with background playback, media notifications, download manager, offline playback, caching and Picture-in-Picture mode."
