package alibaba.filters;

import alibaba.PlaySet;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;

public interface Filter {
public PlaySet apply(PlaySet ps);
}
