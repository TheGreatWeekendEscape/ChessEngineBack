package com.chess.integration.engine.player;

import com.chess.integration.engine.board.Board;
import com.chess.integration.engine.board.Move;
import com.chess.integration.engine.board.Tile;
import com.chess.integration.engine.pieces.Alliance;
import com.chess.integration.engine.pieces.Piece;
import com.chess.integration.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {
    public BlackPlayer(Board board, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }

    /**
     * *CONDICIONES DE CASTLEO*
     * <p>
     * - El rey y la torre deben de no haberse movido.
     * - Ni la casilla del rey, ni la casilla en la que acabará, ni las casillas entre estas pueden estar en check.
     * - No puede haber ninguna pieza entre el rey y la torre.
     */
    @Override
    public Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves) {
        List<Move> kingCastles = new ArrayList<>();

        if (this.king.isFirstMove() && !this.isInCheck()) {
            //Calculate king side castle
            if (!this.board.getTile(5).isOccupied() && !this.board.getTile(6).isOccupied()) {
                Tile rookTile = this.board.getTile(7);
                if (rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(5, opponentLegalMoves).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentLegalMoves).isEmpty() &&
                            rookTile.getPiece().getType() == Piece.PieceType.ROOK) {
                        kingCastles.add(new Move.KingsideCastleMove
                                (this.board, this.king, 6, (Rook)rookTile.getPiece(), rookTile.getCoordinate(), 5));
                    }
                }
            }
            //Calculate queen side castle
            if (!this.board.getTile(1).isOccupied()
                    && !this.board.getTile(2).isOccupied()
                    && !this.board.getTile(3).isOccupied()) {
                Tile rookTile = this.board.getTile(0);
                if (rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(2, opponentLegalMoves).isEmpty()
                            && Player.calculateAttacksOnTile(3, opponentLegalMoves).isEmpty()
                            && rookTile.getPiece().getType() == Piece.PieceType.ROOK) {
                        kingCastles.add(new Move.QueensideCastleMove
                                (this.board, this.king, 2, (Rook)rookTile.getPiece(), rookTile.getCoordinate(), 3));
                    }

                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
