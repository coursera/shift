/*
 Copyright 2015 Coursera Inc.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
     http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.coursera.android.shift;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

class EmailUtils {
    public static void sendEmail(Context context,
                                 String[] recipients,
                                 String subject,
                                 String message,
                                 File[] attachments) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

        /* Makes the chooser only show valid email applications
        instead of any app that supports this Intent*/
        emailIntent.setType("message/rfc822");
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        CharSequence charSequenceMessage = message;
        ArrayList<CharSequence> charSequences = new ArrayList<CharSequence>();
        charSequences.add(charSequenceMessage);
        emailIntent.putExtra(Intent.EXTRA_TEXT, charSequences);

        ArrayList<Uri> uris = new ArrayList<Uri>();
        for (File attachment : attachments) {
            if (attachment.exists()) {
                uris.add(Uri.fromFile(attachment));
            }
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
