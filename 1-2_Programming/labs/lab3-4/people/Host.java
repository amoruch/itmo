// Host.java
package people;

import places.*;
import other.*;
import food.*;

public class Host extends Adult {
    public TVShow tvShow;

    public Host() {
        super("Фрекен Бок", Mood.MISERY, new Kitchen(), "Female");
	this.tvShow = new TVShow("телепередача", this.name);
    }
}

