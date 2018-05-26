package com.caskit.desktop_app.recording.audio_capture;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class InputDevices {

    public static String getDefaultMixerName() {
        Mixer.Info info = getDefaultMixer();
        return info != null ? info.getName() : null;
    }

    public static Mixer.Info getDefaultMixer() {
        Map<Mixer.Info, Line.Info> map = getLines();

        Mixer.Info mixerInfo = map.keySet().stream().filter(info -> info.getName().toLowerCase().contains("microph")).findAny().orElse(null);
        if (mixerInfo != null) {
            return mixerInfo;
        }

        return map.keySet().stream().findAny().orElse(null);
    }


    public static List<String> getLineNames() {
        return getLines().keySet().stream().map(Mixer.Info::getName).collect(Collectors.toList());
    }

    public static Line.Info getTargetLineFromName(String name) {

        for (Map.Entry<Mixer.Info, Line.Info> entry : getLines().entrySet()) {
            String lineName = entry.getKey().getName();
            if (lineName == null) {
                continue;
            }

            if (lineName.equals(name)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public static Map<Mixer.Info, Line.Info> getLines() {
        Map<Mixer.Info, Line.Info> map = new HashMap<>();

        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixers){
            Mixer mixer = AudioSystem.getMixer(mixerInfo);

            Line.Info[] infos = mixer.getTargetLineInfo();

            Line.Info info = Arrays.stream(infos).findFirst().orElse(null);
            if (info != null && info.getLineClass() == TargetDataLine.class) {
                map.put(mixerInfo, info);
            }
        }

        return map;
    }

}
