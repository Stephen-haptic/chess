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

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

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
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
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
}
