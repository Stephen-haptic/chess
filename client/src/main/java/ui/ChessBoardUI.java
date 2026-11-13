package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessGame;

import static java.lang.System.out;

public class ChessBoardUI {
    public void drawBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        boolean isWhite = perspective == ChessGame.TeamColor.WHITE;

        int rowStart = isWhite ? 8 : 1;
        int rowEnd = isWhite ? 1 : 8;
        int rowStep = isWhite ? -1 : 1;

        int colStart = isWhite ? 1 : 8;
        int colEnd = isWhite ? 8 : 1;
        int colStep = isWhite ? 1 : -1;

        for (int row = rowStart; row != rowEnd + rowStep; row += rowStep) {
            out.print(row + " ");

            for (int col = colStart; col != colEnd + colStep; col += colStep) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if ((row + col) % 2 == 0) {
                    out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                } else {
                    out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                }

                if (piece == null) {
                    out.print(EscapeSequences.EMPTY);
                } else {
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                    } else {
                        out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
                    }
                    out.print(getPieceSymbol(piece));
                    out.print(EscapeSequences.RESET_TEXT_COLOR);
                }
                out.print(EscapeSequences.RESET_BG_COLOR);
            }
            out.println();
        }
        if (isWhite) {
            out.println("    a   b   c   d   e   f   g   h");
        } else {
            out.println("    h   g   f   e   d   c   b   a");
        }
    }

    private String getPieceSymbol(ChessPiece piece) {
        boolean isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;

        if (isWhite) {
            return switch (piece.getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
        } else {
            return switch (piece.getPieceType()) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
        }
    }
}
