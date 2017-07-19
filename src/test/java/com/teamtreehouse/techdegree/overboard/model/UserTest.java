package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Trang on 7/18/2017.
 */
public class UserTest {
    private Board mBoard;
    private User mQuestionUser;
    private User mAnswerUser;
    private User mBoardUser;
    private Question mQuestion;
    private Answer mAnswer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        mBoard = new Board("IntelliJ IDEA");
        mQuestionUser = mBoard.createUser("Questioner");
        mAnswerUser = mBoard.createUser("Answerer");
        mBoardUser = mBoard.createUser("BoardUser");
        mQuestion = mQuestionUser.askQuestion("Find shortcut");
        mAnswer = mAnswerUser.answerQuestion(mQuestion,"Ctrl+F");

    }

    @Test
    public void questionersReputationGoesUpByFiveIfTheirQuestionIsUpvoted() throws Exception {
        mBoardUser.upVote(mQuestion);

        assertEquals(5,mQuestionUser.getReputation());
    }

    @Test
    public void downVotingQuestionDoesNotAffectReputation() throws Exception {
        mBoardUser.downVote(mQuestion);

        assertEquals(0,mQuestionUser.getReputation());
    }

    @Test
    public void answererReputationGoesUpToTenIfTheirAnswerIsUpVoted() throws Exception {
        mQuestionUser.upVote(mAnswer);

        assertEquals(10,mAnswerUser.getReputation());
    }

    @Test
    public void answererReputationDecrementOneIfTheirAnswerIsDownVoted() throws Exception {
        mBoardUser.downVote(mAnswer);

        assertEquals(-1,mAnswerUser.getReputation());
    }

    @Test
    public void havingAnswerAcceptedGivesAnswererFifteenReputationBoost() throws Exception {
        mQuestionUser.acceptAnswer(mAnswer);

        assertEquals(15,mAnswerUser.getReputation());
    }

    @Test
    public void questionerUpVotingOwnQuestionIsNotAllowed() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        mQuestionUser.upVote(mQuestion);

    }

    @Test
    public void questionerDownVotingOwnQuestionIsNotAllowed() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        mQuestionUser.downVote(mQuestion);
    }

    @Test
    public void answererUpVotingOwnAnswerIsNotAllowed() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        mAnswerUser.upVote(mAnswer);
    }

    @Test
    public void answererDownVotingOwnAnswerIsNotAllowed() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        mAnswerUser.downVote(mAnswer);
    }

    @Test
    public void unauthorizedQuestionerAcceptingAnswerIsNotAllowed() throws Exception {
        String message = String.format("Only %s can accept this answer as it is their question",mQuestionUser.getName());
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage(message);

        mBoardUser.acceptAnswer(mAnswer);
    }

    @Test
    public void questionerInformsAcceptanceAfterAcceptingAnswer() throws Exception {
        mQuestionUser.acceptAnswer(mAnswer);
        String message = String.format("%s accept answer to the question",mQuestionUser.getName());

        assertTrue(message,mAnswer.isAccepted());
    }

}