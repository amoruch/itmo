// NonClassicMove.java
package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

public class NonClassicMove extends PhysicalMove {

    public NonClassicMove() {
        super(Type.PSYCHIC, 100, 1.0, 0, 3);
    }

    @Override
    public void applyOppEffects(Pokemon p) {
        if (Math.random() < 0.5) {
            p.confuse();
        }
    }

    @Override
    public String describe() {
        return "применяет неклассическую психическую атаку";
    }
}
