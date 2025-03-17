package seedu.reserve.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.reserve.testutil.Assert.assertThrows;
import static seedu.reserve.testutil.TypicalReservation.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.reserve.commons.core.GuiSettings;
import seedu.reserve.logic.Messages;
import seedu.reserve.logic.commands.exceptions.CommandException;
import seedu.reserve.model.Model;
import seedu.reserve.model.ReadOnlyReserveMate;
import seedu.reserve.model.ReadOnlyUserPrefs;
import seedu.reserve.model.ReserveMate;
import seedu.reserve.model.reservation.Reservation;
import seedu.reserve.testutil.ReservationBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullCustomer_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_customerAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingCustomerAdded modelStub = new ModelStubAcceptingCustomerAdded();
        Reservation validReservation = new ReservationBuilder().build();

        CommandResult commandResult = new AddCommand(validReservation).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validReservation)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validReservation), modelStub.customersAdded);
    }

    @Test
    public void execute_duplicateCustomer_throwsCommandException() {
        Reservation validReservation = new ReservationBuilder().build();
        AddCommand addCommand = new AddCommand(validReservation);
        ModelStub modelStub = new ModelStubWithCustomer(validReservation);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_RESERVATION, ()
                -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Reservation alice = new ReservationBuilder().withName("Alice").build();
        Reservation bob = new ReservationBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different reservation -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getReserveMateFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setReserveMateFilePath(Path reserveMateFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addReservation(Reservation reservation) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setReserveMate(ReadOnlyReserveMate newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyReserveMate getReserveMate() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasReservation(Reservation reservation) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteReservation(Reservation target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setReservation(Reservation target, Reservation editedReservation) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Reservation> getFilteredReservationList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredReservationList(Predicate<Reservation> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single reservation.
     */
    private class ModelStubWithCustomer extends ModelStub {
        private final Reservation reservation;

        ModelStubWithCustomer(Reservation reservation) {
            requireNonNull(reservation);
            this.reservation = reservation;
        }

        @Override
        public boolean hasReservation(Reservation reservation) {
            requireNonNull(reservation);
            return this.reservation.isSameReservation(reservation);
        }
    }

    /**
     * A Model stub that always accept the reservation being added.
     */
    private class ModelStubAcceptingCustomerAdded extends ModelStub {
        final ArrayList<Reservation> customersAdded = new ArrayList<>();

        @Override
        public boolean hasReservation(Reservation reservation) {
            requireNonNull(reservation);
            return customersAdded.stream().anyMatch(reservation::isSameReservation);
        }

        @Override
        public void addReservation(Reservation reservation) {
            requireNonNull(reservation);
            customersAdded.add(reservation);
        }

        @Override
        public ReadOnlyReserveMate getReserveMate() {
            return new ReserveMate();
        }
    }

}
