package pl.wsb.fitnesstracker.user.api;

import jakarta.annotation.Nullable;

/**
 * @param Id
 * @param firstName
 * @param lastName
 */
record SimpleUserDto(@Nullable Long Id, String firstName, String lastName) {

}
