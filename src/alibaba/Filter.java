package alibaba;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;

public interface Filter {
public void filter(AudioContext ac,PlaySet ps);
}