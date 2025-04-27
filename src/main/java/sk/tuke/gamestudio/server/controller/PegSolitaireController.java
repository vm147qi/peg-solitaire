package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.pegsolitaire.core.*;
import sk.tuke.gamestudio.service.ScoreService;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class PegSolitaireController {

    @Autowired
    private ScoreService scoreService;

    private int selectedLevelIndex = 0;
    private final BoardShape[] levels = {BoardShape.TRIANGULAR, BoardShape.CLASSIC, BoardShape.EUROPEAN};

    private GameBoard game = new GameBoard(BoardShape.CLASSIC);

    private Integer selectedRow = null;
    private Integer selectedCol = null;

    private String username = null;

    @RequestMapping("/pegsolitaire")
    public String pegsolitaire(@RequestParam(value = "fromRow", required = false) Integer fromRow,
                               @RequestParam(value = "fromCol", required = false) Integer fromCol,
                               @RequestParam(value = "toRow", required = false) Integer toRow,
                               @RequestParam(value = "toCol", required = false) Integer toCol,
                               Model model, HttpSession session) {

        if (fromRow != null && fromCol != null && toRow == null && toCol == null) {
            boolean hasValidMove = game.getValidMoves().stream()
                    .anyMatch(move -> move.getFromRow() == fromRow && move.getFromCol() == fromCol);

            if (hasValidMove) {
                if (selectedRow != null && selectedCol != null &&
                        selectedRow.equals(fromRow) && selectedCol.equals(fromCol)) {
                    selectedRow = null;
                    selectedCol = null;
                } else {
                    selectedRow = fromRow;
                    selectedCol = fromCol;
                }
            } else {
                selectedRow = null;
                selectedCol = null;
            }
        } else if (fromRow != null && fromCol != null && toRow != null && toCol != null) {
            Move move = new Move(fromRow, fromCol, toRow, toCol);
            if (move.isValid(game)) {
                game.applyMove(move);
            }
            selectedRow = null;
            selectedCol = null;
        }

        int remainingPegs = game.getRemainingPegs();
        model.addAttribute("remainingPegs", remainingPegs);

        if (remainingPegs == 1 || remainingPegs == 0 || game.getValidMoves().isEmpty()) {
            Score score = new Score();
            score.setPlayer(username);
            score.setPoints(game.calculateScore());
            score.setGame("PegSolitaire");
            score.setPlayedOn(new Date());
            scoreService.addScore(score);
        }

        if (remainingPegs <= 0 || game.getValidMoves().isEmpty()) {
            saveScore(session);
        }

        model.addAttribute("game", game);
        model.addAttribute("boardHtml", getHtmlField());
        model.addAttribute("score", game.calculateScore());
        model.addAttribute("gameState", game.getState());
        model.addAttribute("remainingPegs", game.getRemainingPegs());
        return "pegsolitaire";
    }

    public String getHtmlField() {
        StringBuilder sb = new StringBuilder("<table class='board'>");
        List<Move> validMoves = game.getValidMoves();

        final Integer selRow = selectedRow;
        final Integer selCol = selectedCol;

        for (int row = 0; row < game.getHeight(); row++) {
            sb.append("<tr>");

            for (int col = 0; col < game.getWidth(); col++) {
                Tile tile = game.getTile(row, col);
                if (tile == null || tile.getState() == TileState.NON_PLAYABLE) {
                    sb.append("<td class='nonplayable'></td>");
                    continue;
                }

                String tileClass = tile.getState().name().toLowerCase();
                String extraClass = "";
                String content = "";

                final int currentRow = row;
                final int currentCol = col;


                if (selRow != null && selCol != null) {
                    if (row == selRow && col == selCol) {
                        extraClass = " selected";
                    }

                    boolean isMoveTarget = validMoves.stream().anyMatch(move ->
                            move.getFromRow() == selRow && move.getFromCol() == selCol &&
                                    move.getToRow() == currentRow && move.getToCol() == currentCol);

                    if (isMoveTarget) {
                        extraClass = " movable";
                        content = String.format(
                                "<a href='/pegsolitaire?fromRow=%d&fromCol=%d&toRow=%d&toCol=%d'>&nbsp;</a>",
                                selRow, selCol, row, col
                        );
                    }
                }

                if (tile.getState() == TileState.PEG) {
                    content = String.format(
                            "<a href='/pegsolitaire?fromRow=%d&fromCol=%d'>&nbsp;</a>",
                            row, col
                    );
                }

                sb.append(String.format("<td class='%s%s'>%s</td>", tileClass, extraClass, content));
            }
            sb.append("</tr>");
        }

        sb.append("</table>");
        return sb.toString();
    }

    //
    @RequestMapping("/pegsolitaire/new")
    public String newGame() {
        game = new GameBoard(levels[selectedLevelIndex]);
        selectedRow = null;
        selectedCol = null;
        return "redirect:/pegsolitaire";
    }

    //
    @RequestMapping("/pegsolitaire/start")
    public String startScreen() {
        return "fragments/pegsolitaire_start";
    }
    //
    @RequestMapping("/pegsolitaire/menu")
    public String menuScreen() {
        return "fragments/pegsolitaire_menu";
    }
    //
    @RequestMapping("/pegsolitaire/rules")
    public String rulesScreen() {
        return "fragments/pegsolitaire_rules";
    }
    //
    @RequestMapping("/pegsolitaire/comics")
    public String comicsScreen(Model model) {
        if (username == null) {
            return "redirect:/pegsolitaire/login";
        }
        return "fragments/pegsolitaire_comics";
    }
    //
    @RequestMapping("/pegsolitaire/reviews")
    public String reviewsScreen() {
        return "fragments/pegsolitaire_reviews";
    }
    //
    @RequestMapping("/pegsolitaire/leaderboard")
    public String leaderScreen(Model model) {
        List<Score> topScores = scoreService.getTopScores("PegSolitaire");
        List<Score> top8Scores = topScores.stream().limit(8).collect(Collectors.toList());
        model.addAttribute("scores", top8Scores);
        return "fragments/pegsolitaire_leaderboard";
    }


    @RequestMapping("/pegsolitaire/login")
    public String loginScreen() {
        return "fragments/pegsolitaire_login";
    }
    @RequestMapping("/pegsolitaire/loginSubmit")
    public String loginSubmit(@RequestParam("username") String username) {
        this.username = username;
        return "redirect:/pegsolitaire/comics";
    }
    @RequestMapping("/pegsolitaire/saveScore")
    public String saveScore(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null && game.getRemainingPegs() <= 0) {
            Score score = new Score();
            score.setPlayer(username);
            score.setPoints(game.calculateScore());
            score.setGame("PegSolitaire");
            score.setPlayedOn(new Date());
            scoreService.addScore(score);
        }
        return "redirect:/pegsolitaire/leaderboard";
    }



    //
    @RequestMapping("/pegsolitaire/levels")
    public String levelSelectScreen(Model model) {
        model.addAttribute("currentLevel", levels[selectedLevelIndex]);
        model.addAttribute("imagePath", "/images/" + levels[selectedLevelIndex].name().toLowerCase() + ".png");
        return "fragments/pegsolitaire_levels";
    }
    @RequestMapping("/pegsolitaire/levels/prev")
    public String previousLevel() {
        selectedLevelIndex = (selectedLevelIndex - 1 + levels.length) % levels.length;
        return "redirect:/pegsolitaire/levels";
    }
    @RequestMapping("/pegsolitaire/levels/next")
    public String nextLevel() {
        selectedLevelIndex = (selectedLevelIndex + 1) % levels.length;
        return "redirect:/pegsolitaire/levels";
    }
    @RequestMapping("/pegsolitaire/startSelected")
    public String startSelectedLevel() {
        game = new GameBoard(levels[selectedLevelIndex]);
        return "redirect:/pegsolitaire";
    }
    @RequestMapping("/pegsolitaire/selectLevel")
    public String selectLevel(@RequestParam("level") String levelName) {
        BoardShape selected = BoardShape.valueOf(levelName);
        game = new GameBoard(selected);
        return "redirect:/pegsolitaire";
    }


    //
    @RequestMapping("/pegsolitaire/exit")
    public String exitScreen() {
        return "fragments/pegsolitaire_start";
    }
    //
    @RequestMapping("/pegsolitaire/undo")
    public String undoMove() {
        game.undoMove();
        return "redirect:/pegsolitaire";
    }
}