package org.team100.lib.commands.retro;

import org.team100.lib.retro.Illuminator;

import edu.wpi.first.wpilibj2.command.Command;

public class LedOn extends Command {
    private final Illuminator illuminator;

    public LedOn(Illuminator i) {
        illuminator = i;
    }

    @Override
    public void initialize() {
        illuminator.set(1);
    }

    @Override
    public void end(boolean interrupted) {
        illuminator.set(0);
    }
}
