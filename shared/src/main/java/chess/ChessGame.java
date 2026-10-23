package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> validMoves =  new ArrayList<>();
        var place = board.getPiece(startPosition);
        if (place == null) {
            return null;
        }
        else {
            validMoves.addAll(place.pieceMoves(board, startPosition));

            Iterator<ChessMove> testMoves = validMoves.iterator();

            while (testMoves.hasNext()) {
                ChessMove testMove = testMoves.next();
                ChessPosition preMovePosition = testMove.getStartPosition();
                ChessPosition preMoveDestination = testMove.getEndPosition();
                var preMovePiece = board.getPiece(preMovePosition);
                var preMoveDestPiece = board.getPiece(preMoveDestination);
                tempMove(testMove);
                if (isInCheck(place.getTeamColor())) {
                    testMoves.remove();
                }
                board.addPiece(preMovePosition, preMovePiece);
                board.addPiece(preMoveDestination, preMoveDestPiece);
            }

            return validMoves;
        }
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        Collection<ChessMove> validMoves = validMoves(startPosition);
        if (validMoves != null && validMoves.contains(move)) {
            var place = board.getPiece(startPosition);
            ChessGame.TeamColor pieceTeam = place.getTeamColor();
            if (pieceTeam != teamTurn) {
                throw new InvalidMoveException("Not a valid move.");
            }
            if (move.getPromotionPiece() != null) {
                board.addPiece(endPosition, new ChessPiece(place.getTeamColor(), promotionPiece));
            }
            else {
                board.addPiece(endPosition, place);
            }
            board.addPiece(startPosition, null);
            if (teamTurn == TeamColor.WHITE) {
                teamTurn = TeamColor.BLACK;
            }
            else {
                teamTurn = TeamColor.WHITE;
            }
        }
        else {
            throw new InvalidMoveException("Not a valid move.");
        }
    }

    /**
     * Makes a temporary move in the chess game
     *
     * @param move chess move to perform

     */
    private void tempMove(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        var place = board.getPiece(startPosition);
        if (move.getPromotionPiece() != null) {
            board.addPiece(endPosition, new ChessPiece(place.getTeamColor(), promotionPiece));
        }
        else {
            board.addPiece(endPosition, place);
        }
        board.addPiece(startPosition, null);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != teamColor) {
                    if (canAttackKing(position, kingPosition)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && !hasAnyValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && !hasAnyValidMoves(teamColor);
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    private boolean hasAnyValidMoves(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(position);

                    if (moves != null && !moves.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition findKing(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null
                        && piece.getPieceType() == ChessPiece.PieceType.KING
                        && piece.getTeamColor() == teamColor) {
                    return position;
                }
            }
        }
        return null;
    }

    private boolean canAttackKing(ChessPosition position, ChessPosition kingPosition) {
        ChessPiece piece = board.getPiece(position);
        Collection<ChessMove> enemyMoves = piece.pieceMoves(board, position);
        for (ChessMove move : enemyMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }
}
