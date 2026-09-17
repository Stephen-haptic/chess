package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        if (this.getPieceType() == PieceType.BISHOP) {
            // Bishop movement down and to the right
            fullLineMove(board, myPosition, 1, 1, moves);
            // Bishop movement down and to the left
            fullLineMove(board, myPosition, 1, -1, moves);
            // Bishop movement up and to the left
            fullLineMove(board, myPosition, -1, -1, moves);
            // Bishop movement up and to the right
            fullLineMove(board, myPosition, -1, 1, moves);
        }
        if (this.getPieceType() == PieceType.ROOK) {
            // Rook movement right
            fullLineMove(board, myPosition, 0, 1, moves);
            // Rook movement left
            fullLineMove(board, myPosition, 0, -1, moves);
            // Rook movement down
            fullLineMove(board, myPosition, 1, 0, moves);
            // Rook movement up
            fullLineMove(board, myPosition, -1, 0, moves);
        }
        if (this.getPieceType() == PieceType.QUEEN) {
            // Queen movement down and to the right
            fullLineMove(board, myPosition, 1, 1, moves);
            // Queen movement down and to the left
            fullLineMove(board, myPosition, 1, -1, moves);
            // Queen movement up and to the left
            fullLineMove(board, myPosition, -1, -1, moves);
            // Queen movement up and to the right
            fullLineMove(board, myPosition, -1, 1, moves);
            // Queen movement right
            fullLineMove(board, myPosition, 0, 1, moves);
            // Queen movement left
            fullLineMove(board, myPosition, 0, -1, moves);
            // Queen movement down
            fullLineMove(board, myPosition, 1, 0, moves);
            // Queen movement up
            fullLineMove(board, myPosition, -1, 0, moves);
        }
        if (this.getPieceType() == PieceType.KNIGHT) {
            spotCheckMove(board, myPosition, 2, 1, moves);
            spotCheckMove(board, myPosition, 1, 2, moves);
            spotCheckMove(board, myPosition, -2, 1, moves);
            spotCheckMove(board, myPosition, -1, 2, moves);
            spotCheckMove(board, myPosition, 2, -1, moves);
            spotCheckMove(board, myPosition, 1, -2, moves);
            spotCheckMove(board, myPosition, -2, -1, moves);
            spotCheckMove(board, myPosition, -1, -2, moves);
        }
        if (this.getPieceType() == PieceType.KING) {
            spotCheckMove(board, myPosition, 0, 1, moves);
            spotCheckMove(board, myPosition, 0, -1, moves);
            spotCheckMove(board, myPosition, 1, 0, moves);
            spotCheckMove(board, myPosition, -1, 0, moves);
            spotCheckMove(board, myPosition, 1, 1, moves);
            spotCheckMove(board, myPosition, -1, -1, moves);
            spotCheckMove(board, myPosition, -1, 1, moves);
            spotCheckMove(board, myPosition, 1, -1, moves);
        }
        if (this.getPieceType() == PieceType.PAWN && this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            pawnMove(board, myPosition, 1, 2, 8, moves);
        }
        if (this.getPieceType() == PieceType.PAWN && this.getTeamColor() == ChessGame.TeamColor.BLACK) {
            pawnMove(board, myPosition, -1, 7, 1, moves);
        }
        return moves;
    }

    public void fullLineMove(ChessBoard board, ChessPosition myPosition, int rowDirection, int colDirection, ArrayList<ChessMove> moves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        row += rowDirection;
        col += colDirection;
        while (row <= 8 && col <= 8 && row >= 1 && col >= 1) {
            var place = board.getPiece(new ChessPosition(row, col));
            if (place == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
            }
            else if (place.getTeamColor() != this.getTeamColor()) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                break;
            }
            else {
                break;
            }
            row += rowDirection;
            col += colDirection;
        }
    }

    public void spotCheckMove(ChessBoard board, ChessPosition myPosition, int rowOffset, int colOffset, ArrayList<ChessMove> moves) {
        int row = myPosition.getRow() + rowOffset;
        int col = myPosition.getColumn() + colOffset;
        if (row <= 8 && col <= 8 && row >= 1 && col >= 1) {
            var place = board.getPiece(new ChessPosition(row, col));
            if (place == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
            }
            else if (place.getTeamColor() != this.getTeamColor()) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
            }
        }
    }

    public void pawnMove(ChessBoard board, ChessPosition myPosition, int direction, int startingRow, int promotionRow, ArrayList<ChessMove> moves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row == startingRow) {
            for (int i = direction; i != direction * 3; i += direction) {
                var place = board.getPiece(new ChessPosition(row + i, col));
                if (place == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + i, col), null));
                }
                else {
                    break;
                }
            }
        }
        else {
            row = myPosition.getRow() + direction;
            if (row <= 8 && col <= 8 && row >= 1 && col >= 1) {
                var place = board.getPiece(new ChessPosition(row, col));
                if (place == null && row == promotionRow) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), PieceType.KNIGHT));
                }
                else if (place == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                }
            }
        }
        row = myPosition.getRow() + direction;
        for (int i = -1; i < 2; i += 2) {
            col = myPosition.getColumn() + i;
            if (row <= 8 && col <= 8 && row >= 1 && col >= 1) {
                var pawnCap = board.getPiece(new ChessPosition(row, col));
                if (pawnCap != null && pawnCap.getTeamColor() != this.getTeamColor() && row == promotionRow) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), PieceType.KNIGHT));
                }
                else if (pawnCap != null && pawnCap.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                }
            }
        }
    }
}
