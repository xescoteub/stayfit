# Manage Firebase users

## Step 1
The first thing you'd need to do is ensure that users have access to only the data they store. 
To do this, go to Database/Rules and change your rules to this:

`
{
"rules": {
  "my_app_user": {
    "$uid": {
      ".write": "auth != null && auth.uid == $uid",
      ".read": "auth != null && auth.uid == $uid"
     }
   }
 }
}
`

## Step 2
You have to create a database table say "users".
On successful signin, signup for first time you have to create a new row in user table.

`
public static void writeNewUser(DatabaseReference databaseReference, String userId, String name, String email, int accountType) 
{
    User user = new User(name, email, accountType);

    databaseReference.child("users").child(userId).setValue(user);
}
`
### Write extra info
User user = new User();
    user.setAddressTwo("address_two");
    user.setPhoneOne("phone_one");
    ...

    mDatabaseReference.child("my_app_user").child(firebaseUser.getUid()).setValue(user).
            addOnCompleteListener(DetailsCaptureActivity.this,
                               new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        ...
## Note:
The name of the child **my_app_user** must match both in your code and the Firebase rules else it won't persist.