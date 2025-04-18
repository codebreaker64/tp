package seedu.reserve.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.reserve.logic.Messages.MESSAGE_NO_RESERVATIONS;
import static seedu.reserve.logic.Messages.MESSAGE_RESERVATIONS_LISTED_OVERVIEW;
import static seedu.reserve.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.reserve.testutil.TypicalReservation.CARL;
import static seedu.reserve.testutil.TypicalReservation.ELLE;
import static seedu.reserve.testutil.TypicalReservation.FIONA;
import static seedu.reserve.testutil.TypicalReservation.getTypicalReserveMate;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.reserve.model.Model;
import seedu.reserve.model.ModelManager;
import seedu.reserve.model.UserPrefs;
import seedu.reserve.model.reservation.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalReserveMate(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalReserveMate(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different reservation -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noReservationFound() {
        String expectedMessage = MESSAGE_NO_RESERVATIONS;
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredReservationList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredReservationList());
    }

    @Test
    public void execute_multipleKeywords_multipleReservationsFound() {
        String expectedMessage = String.format(MESSAGE_RESERVATIONS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Kunz Elle");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredReservationList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(FIONA, CARL, ELLE), model.getFilteredReservationList());
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
