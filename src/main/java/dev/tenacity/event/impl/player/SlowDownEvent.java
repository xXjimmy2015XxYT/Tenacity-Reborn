package dev.tenacity.event.impl.player;

import dev.tenacity.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class SlowDownEvent extends Event {
    private float forwardMult;
    private float strafeMult;
}
