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

import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class PegSolitaireController {

    @Autowired
    private ScoreService scoreService;

    private GameBoard game = new GameBoard(BoardShape.CLASSIC);

    private Integer selectedRow = null;
    private Integer selectedCol = null;

    @RequestMapping("/pegsolitaire")
    public String pegsolitaire(@RequestParam(value = "fromRow", required = false) Integer fromRow,
                               @RequestParam(value = "fromCol", required = false) Integer fromCol,
                               @RequestParam(value = "toRow", required = false) Integer toRow,
                               @RequestParam(value = "toCol", required = false) Integer toCol,
                               Model model) {

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

        model.addAttribute("game", game);
        model.addAttribute("boardHtml", getHtmlField());
        model.addAttribute("score", game.calculateScore());
        model.addAttribute("gameState", game.getState());
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
    public String comicsScreen() {
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
        model.addAttribute("scores", topScores);
        return "fragments/pegsolitaire_leaderboard";
    }
    //
    @RequestMapping("/pegsolitaire/levels")
    public String levelSelectScreen() {
        return "fragments/pegsolitaire_levels";
    }
    //
    @RequestMapping("/pegsolitaire/new")
    public String newGame() {
        game = new GameBoard(BoardShape.CLASSIC);
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
