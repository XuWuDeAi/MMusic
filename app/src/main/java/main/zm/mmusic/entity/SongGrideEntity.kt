package main.zm.mmusic.entity

import android.graphics.drawable.Drawable
import org.json.JSONArray

/**
 * Created by zm on 2018/9/11.
 */

class SongGrideEntity {
    var songIm: String
    var songName: String

    constructor(songIm: String, songName: String) {
        this.songIm = songIm
        this.songName = songName
    }
}
