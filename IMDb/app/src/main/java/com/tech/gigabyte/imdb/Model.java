package com.tech.gigabyte.imdb;

/**
 * Created by ASHUTOSH on 18-JULY-2017.
 *
 */

class Model {
    private int movie_id;
    private int is_favorite;
    private int is_watchlist;

    public Model(int movie_id, int is_favorite, int is_watchlist) {
        this.movie_id = movie_id;
        this.is_favorite = is_favorite;
        this.is_watchlist = is_watchlist;
    }


    int getMovie_id() {
        return movie_id;
    }

    void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    int getIs_favorite() {
        return is_favorite;
    }

    void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }

    int getIs_watchlist() {
        return is_watchlist;
    }

    void setIs_watchlist(int is_watchlist) {
        this.is_watchlist = is_watchlist;
    }

    Model() {

    }


}
