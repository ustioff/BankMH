package me.usti.banks_hold;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Team {
    private Scoreboard board;
    private org.bukkit.scoreboard.Team team;

    public Team() {
        final ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final org.bukkit.scoreboard.Team team = board.registerNewTeam("NoCollision");
        team.setOption(org.bukkit.scoreboard.Team.Option.COLLISION_RULE, org.bukkit.scoreboard.Team.OptionStatus.NEVER);
        this.team = team;
        this.board = board;
    }

    public org.bukkit.scoreboard.Team getTeam() {
        return this.team;
    }

    public Scoreboard getBoard() {
        return this.board;
    }
}
