package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessGame;

import static java.lang.System.out;

public class ChessBoardUI {
    public void drawBoard(ChessBoard board) {
        for (int row = 8; row >= 1; row--) {
            out.print(row + " ");

            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece == null) {
                    out.print(" . ");
                } else {
                    out.print(" " + getPieceSymbol(piece) + " ");
                }
            }
            out.println();
        }
        out.println("   a  b  c  d  e  f  g  h");
    }

    private String getPieceSymbol(ChessPiece piece) {
        String symbol = switch (piece.getPieceType()) {
            case KING -> "K";
            case QUEEN -> "Q";
            case ROOK -> "R";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case PAWN -> "P";
        };

        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            return symbol.toLowerCase();
        }

        return symbol;
    }
}
