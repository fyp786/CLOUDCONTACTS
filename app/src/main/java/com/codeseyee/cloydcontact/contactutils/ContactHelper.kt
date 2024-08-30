package com.codeseyee.cloydcontact.contactutils

import android.Manifest
import android.content.ContentProviderOperation
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.OperationApplicationException
import android.content.pm.PackageManager
import android.os.Build
import android.os.RemoteException
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import com.codeseyee.cloydcontact.Model.User

class ContactHelper(private val context: Context) {

    fun saveToContact(contact: Contact): Boolean {
        return saveToContact(
            contact.displayName.orEmpty(),
            contact.phoneNumbers,
            contact.emails.orEmpty(),
            contact.companyName.orEmpty(),
            contact.companyTitle.orEmpty(),
            contact.addresses.orEmpty(),
            "",
            "",
            contact.websites.orEmpty()
        )
    }

    fun saveToContact(user: User): Boolean {
        return saveToContact(
            user.name,
            user.phone,
            user.email,
            user.companyName,
            user.accountType,
            user.profileImageUrl,
            "",
            "",
            ""  // Assuming website is not required for User
        )
    }

    private fun saveToContact(
        name: String, number: String, email: String, companyName: String,
        jobTitle: String, address: String, city: String, country: String, website: String
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        val ops = arrayListOf<ContentProviderOperation>()
        val rawContactInsertIndex = ops.size

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            .build())

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
            .build())

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
            .build())

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
            .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
            .build())

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Website.DATA, website)
            .build())

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, address)
            .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, city)
            .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, country)
            .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
            .build())

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, companyName)
            .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
            .build())

        return try {
            context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            true
        } catch (e: RemoteException) {
            e.printStackTrace()
            false
        } catch (e: OperationApplicationException) {
            e.printStackTrace()
            false
        }
    }
    fun getUsersWhichAreContacts(): List<User> {
        // Implement this method to return a list of users that are also contacts
        return emptyList()
    }

//    fun getCurrentUser(): User {
//        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//        val id = sharedPreferences.getLong("userId", 0)
//        val name = sharedPreferences.getString("userName", "Default User") ?: "Default User"
//        val email = sharedPreferences.getString("userEmail", "default@example.com") ?: "default@example.com"
//
//        return User(id, name, email)
//    }



    companion object {
        fun deleteContactFromPhonebook(context: Context, contacts: List<Contact>) {
            if (contacts.isNullOrEmpty()) return
            val ops = ArrayList<ContentProviderOperation>()
            val cr = context.contentResolver
            for (contact in contacts) {
                ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                    .withSelection(
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?",
                        arrayOf(contact.id.toString())
                    )
                    .build())
            }
            try {
                cr.applyBatch(ContactsContract.AUTHORITY, ops)
            } catch (e: RemoteException) {
                e.printStackTrace()
            } catch (e: OperationApplicationException) {
                e.printStackTrace()
            }
        }
    }
}
