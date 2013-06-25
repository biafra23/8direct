package com.jaeckel.direct.nfc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;

public class DistributionNfc
{

   /**
    * <code>MIME_TYPEDETAILS</code> indicates/is used for.
    */
   public static final String MIME_TYPEDETAILS = "application/vdn.com.jaeckel.direct.distribution";
   private static final String TAG = "DistributionNfc";

   @TargetApi(14)
   public static void registerNfcMessage(Activity ctx, String mimeType, String[] payload)
   {
      if (Build.VERSION.SDK_INT < 14)
         return; // To old a SDK version
      NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(ctx);
      if (nfcAdapter == null)
         return; // NFC not available on this device

      NdefMessage ndefMessage = createMessage(ctx, mimeType, payload);
      nfcAdapter.setNdefPushMessage(ndefMessage, ctx);

   }

   @TargetApi(14)
   public static void registerNfcMessageCallback(Activity activity, String mimeType, NfcPayloadCallback contentSupllier)
   {
      if (Build.VERSION.SDK_INT < 14)
         return; // To old a SDK version
      NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
      if (nfcAdapter == null)
         return; // NFC not available on this device

      CreateNdefMessageCallback callbacks = new CreateNdefMessageCallback(activity, mimeType, contentSupllier);
      nfcAdapter.setNdefPushMessageCallback(callbacks, activity);
      nfcAdapter.setOnNdefPushCompleteCallback(callbacks, activity);
   }

   @TargetApi(14)
   public static Object readNfcMessage(Intent intent, String mimeType)
   {
      if (Build.VERSION.SDK_INT < 14)
         return null; // To old a SDK version
      Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
      for (int i = 0; i < rawMsgs.length; i++)
      {
         NdefMessage msg = (NdefMessage) rawMsgs[i];
         for (NdefRecord record : msg.getRecords())
         {
            try
            {
               String nfcMimeType = new String(record.getType(), "US-ASCII");
               if (mimeType.equals(nfcMimeType))
               {
                  return DistributionNfc.deserialize(record.getPayload());
               }
            }
            catch (UnsupportedEncodingException e)
            {
               Log.e(TAG, "Failed to read NFC message", e);
            }
         }
      }
      return null;
   }

   private static NdefMessage createMessage(Activity ctx, String mimeType, Object payload)
   {
      NdefMessage ndefMessage = null;
      try
      {
         NdefRecord aaRecord = NdefRecord.createApplicationRecord(ctx.getPackageName());
         byte[] mimeData = DistributionNfc.serialize(payload);
         byte[] typeBytes = mimeType.getBytes("US-ASCII");
         NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, typeBytes, null, mimeData);

         ndefMessage = new NdefMessage(new NdefRecord[] { mimeRecord, aaRecord });
      }
      catch (IOException e)
      {
         Log.e(TAG, "Failed to register NfcMessage", e);
      }
      return ndefMessage;
   }

   private static byte[] serialize(Object obj)
   {
      ByteArrayOutputStream b = new ByteArrayOutputStream();
      ObjectOutputStream o;
      byte[] bytes = null;
      try
      {
         o = new ObjectOutputStream(b);
         o.writeObject(obj);
         bytes = b.toByteArray();
      }
      catch (IOException e)
      {
         Log.e(TAG, "Failed to serialize object", e);
      }
      return bytes;
   }

   private static Object deserialize(byte[] bytes)
   {
      ByteArrayInputStream b = new ByteArrayInputStream(bytes);
      ObjectInputStream o;
      Object object = null;
      try
      {
         o = new ObjectInputStream(b);
         object = o.readObject();
      }
      catch (StreamCorruptedException e)
      {
         Log.e(TAG, "Failed to deserialize object", e);
      }
      catch (IOException e)
      {
         Log.e(TAG, "Failed to deserialize object", e);
      }
      catch (ClassNotFoundException e)
      {
         Log.e(TAG, "Failed to deserialize object", e);
      }
      return object;
   }

   private static class CreateNdefMessageCallback implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback
   {

      private NfcPayloadCallback callback;
      private String mimeType;
      private WeakReference<Activity> activityRef;
      public Object payload;

      public CreateNdefMessageCallback(Activity activity, String mimeType, NfcPayloadCallback callback)
      {
         this.activityRef = new WeakReference<Activity>(activity);
         this.mimeType = mimeType;
         this.callback = callback;
      }

      @Override
      public NdefMessage createNdefMessage(NfcEvent event)
      {
         Activity activity = activityRef.get();
         NdefMessage message = null;
         if (activity != null)
         {
            payload = callback.preparePayload();
            message = createMessage(activity, mimeType, payload);
         }
         return message;
      }

      @Override
      public void onNdefPushComplete(NfcEvent event)
      {
         callback.didTransferPayload(payload);
      }
   }

   public interface NfcPayloadCallback
   {
      Object preparePayload();

      void didTransferPayload(Object payload);
   }
}
