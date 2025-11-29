package use_case.change_password;

import Entities.User;
import Entities.UserFactory;

/**
 * The Change Password Interactor.
 */
public class ChangePasswordInteractor implements ChangePasswordInputBoundary {
    private final ChangePasswordUserDataAccessInterface userDataAccessObject;
    private final ChangePasswordOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public ChangePasswordInteractor(ChangePasswordUserDataAccessInterface changePasswordDataAccessInterface,
                                    ChangePasswordOutputBoundary changePasswordOutputBoundary,
                                    UserFactory userFactory) {
        this.userDataAccessObject = changePasswordDataAccessInterface;
        this.userPresenter = changePasswordOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(ChangePasswordInputData changePasswordInputData) {
        if ("".equals(changePasswordInputData.getPassword())) {
            userPresenter.prepareFailView("New password cannot be empty");
        }
        else {
            // Get the existing user to preserve other fields
            final User existingUser = userDataAccessObject.get(changePasswordInputData.getUsername());

            // Create a new User with the new password and existing fields
            final User updatedUser = userFactory.create(
                    existingUser.getName(),
                    changePasswordInputData.getPassword(),
                    existingUser.getAge(),
                    existingUser.getHeight(),
                    existingUser.getWeight()
            );

            userDataAccessObject.changePassword(updatedUser);

            final ChangePasswordOutputData changePasswordOutputData =
                    new ChangePasswordOutputData(updatedUser.getName());
            userPresenter.prepareSuccessView(changePasswordOutputData);
        }
    }
}
