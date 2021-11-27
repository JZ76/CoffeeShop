package util;

/*
 * F21AS - Coursework group 4
 * 
 * Name class
 * 
 * Used for customer names
 * 
 */



public class Name {

    private final String firstName;
    private final String middleName;
    private final String lastName;

    public Name(String fullName) {
        int space1 = fullName.indexOf(' ');
        firstName = fullName.substring(0, space1).replace(" ", "");

        int space2 = fullName.lastIndexOf(' ');
        if (space1 == space2) {
            middleName = "";
        } else {
            middleName = fullName.substring(space1 + 1, space2).replace(" ", "");
        }

        lastName = fullName.substring(space2 + 1).replace(" ", "");
    }

    //returns name in the format: "Michael Jackson"
    public String getFullName() {
        try {
            String result = firstName + " ";
            if (!middleName.equals("")) {
                result += middleName + " ";
            }
            result += lastName;
            return result;
        } catch (NullPointerException name) {
            return "WRONG NAME FORMAT";
        }
    }

    //to generate a format that "Jackson, Michael"
    public String getLCF() {
        return lastName + ", " + firstName;
    }

    //get the first letter of lastname and be used for sort list by this
    public String getLastName() {
        return lastName.substring(0, 1);
    }
}
