package pl.wsb.fitnesstracker.user.api;

import jakarta.annotation.Nullable;

record SimpleUserDto(@Nullable Long Id, String firstName, String lastName) {

}
