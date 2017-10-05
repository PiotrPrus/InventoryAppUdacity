package com.example.piotr.inventoryappudacity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.piotr.inventoryappudacity.data.InventoryDbHelper;
import com.example.piotr.inventoryappudacity.data.InventoryItem;

import static com.example.piotr.inventoryappudacity.data.InventoryContract.InventoryEntry.*;


public class DetailsActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_TO_READ_EXTERNAL_STORAGE = 1;
    private static final int DEFAULT_ITEM_ID = 0;
    private InventoryDbHelper dbHelper;
    EditText nameEditText;
    EditText priceEditText;
    EditText quantityEditText;
    EditText supplierNameEditText;
    EditText supplierPhoneEditText;
    EditText supplierEmailEditText;
    long currentItemId;
    ImageButton decreaseQuantityButton;
    ImageButton increaseQuantityButton;
    Button imageButton;
    ImageView imageView;
    Uri actualImageUri;
    private static final int PICK_IMAGE_REQUEST = 0;
    Boolean itemHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initViews();
        initListeners();
        dbHelper = new InventoryDbHelper(this);
        currentItemId = getIntent().getLongExtra("itemId", DEFAULT_ITEM_ID);

        if (currentItemId == 0) {
            setTitle(getString(R.string.title_new_item));
        } else {
            setTitle(getString(R.string.title_edit_item));
            addValuesToCursor(currentItemId);
        }

    }

    private void initViews() {
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        priceEditText = (EditText) findViewById(R.id.price_edit_text);
        quantityEditText = (EditText) findViewById(R.id.quantity_edit_text);
        supplierNameEditText = (EditText) findViewById(R.id.supplier_name_edit_text);
        supplierPhoneEditText = (EditText) findViewById(R.id.supplier_phone_edit_text);
        supplierEmailEditText = (EditText) findViewById(R.id.supplier_email_edit_text);
        decreaseQuantityButton = (ImageButton) findViewById(R.id.decrease_quantity_button);
        increaseQuantityButton = (ImageButton) findViewById(R.id.increase_quantity);
        imageButton = (Button) findViewById(R.id.select_image_button);
        imageView = (ImageView) findViewById(R.id.image_view);
    }

    private void initListeners() {
        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityDecreasedByOne();
                itemHasChanged = true;
            }
        });

        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityIncreasedByOne();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryToOpenImageSelector();
                itemHasChanged = true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!itemHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesAlertDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesAlertDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message);
        builder.setPositiveButton(R.string.yes, discardButtonClickListener);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void tryToOpenImageSelector() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_TO_READ_EXTERNAL_STORAGE);
            return;
        }
        openImageSelector();
    }

    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_TO_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageSelector();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                actualImageUri = data.getData();
                imageView.setImageURI(actualImageUri);
                imageView.invalidate();
            }
        }
    }

    private void quantityDecreasedByOne() {
        String previousQuantityValue = quantityEditText.getText().toString();
        int previousValue;
        if (previousQuantityValue.isEmpty() || previousQuantityValue.equals("0")) {
            return;
        } else {
            previousValue = Integer.parseInt(previousQuantityValue);
            quantityEditText.setText(String.valueOf(previousValue - 1));
        }
    }

    private void quantityIncreasedByOne() {
        String previousQuantityValue = quantityEditText.getText().toString();
        int previousValue;
        if (previousQuantityValue.isEmpty()) {
            previousValue = 0;
        } else {
            previousValue = Integer.parseInt(previousQuantityValue);
        }
        quantityEditText.setText(String.valueOf(previousValue + 1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details_activity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentItemId == 0) {
            MenuItem deleteOneItem = menu.findItem(R.id.menu_delete_item);
            MenuItem deleteAllItems = menu.findItem(R.id.menu_delete_all_items);
            MenuItem orderItem = menu.findItem(R.id.menu_order);
            deleteOneItem.setVisible(false);
            deleteAllItems.setVisible(false);
            orderItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                if (!addItemToDatabase()) {
                    return true;
                }
                finish();
                return true;
            case android.R.id.home:
                if (!itemHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(DetailsActivity.this);
                            }
                        };
                showUnsavedChangesAlertDialog(discardButtonClickListener);
                return true;
            case R.id.menu_order:
                showOrderConfirmationDialog();
                return true;
            case R.id.menu_delete_item:
                showDeleteConfirmationDialog(currentItemId);
                return true;
            case R.id.menu_delete_all_items:
                showDeleteConfirmationDialog(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog(final long itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (itemId == 0) {
                    deleteAllItemsFromDatabase();
                } else {
                    deleteOneItemFromDatabase(itemId);
                }
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private int deleteOneItemFromDatabase(long itemId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selection = _ID + "=?";
        String[] selectionArgs = {String.valueOf(itemId)};
        return database.delete(TABLE_NAME, selection, selectionArgs);
    }

    private int deleteAllItemsFromDatabase() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.delete(TABLE_NAME, null, null);
    }

    private void showOrderConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.place_order_message);
        builder.setPositiveButton(R.string.phone, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: " + supplierPhoneEditText.getText().toString().trim()));
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.email, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:" + supplierEmailEditText.getText().toString().trim()));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Repeat order");
                String message = "Please send ASAP more " + nameEditText.getText().toString().trim()
                        + "!";
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean addItemToDatabase() {
        boolean isAllSet = true;
        if (!checkIfValueSet(nameEditText, "name") || !checkIfValueSet(priceEditText, "price")
                || !checkIfValueSet(quantityEditText, "quantity")
                || !checkIfValueSet(supplierNameEditText, "supplier name")
                || !checkIfValueSet(supplierPhoneEditText, "supplier phone")
                || !checkIfValueSet(supplierEmailEditText, "supplier email")) {
            isAllSet = false;
        }
        if (actualImageUri == null && currentItemId == 0) {
            isAllSet = false;
            imageButton.setError("Missing image");
        }
        if (!isAllSet) {
            return false;
        }

        if (currentItemId == 0) {
            InventoryItem item = new InventoryItem(
                    nameEditText.getText().toString().trim(), priceEditText.getText().toString().trim(),
                    Integer.parseInt(quantityEditText.getText().toString().trim()),
                    supplierNameEditText.getText().toString().trim(),
                    supplierPhoneEditText.getText().toString().trim(),
                    supplierEmailEditText.getText().toString().trim(),
                    actualImageUri.toString());
            dbHelper.insertItem(item);

        } else {
            int quantity = Integer.parseInt(quantityEditText.getText().toString().trim());
            dbHelper.updateItem(currentItemId, quantity);
        }
        return true;
    }

    private boolean checkIfValueSet(EditText text, String desc) {
        if (TextUtils.isEmpty(text.getText())) {
            text.setError("Missing product " + desc);
            return false;
        } else {
            text.setError(null);
            return true;
        }
    }

    private void addValuesToCursor(long itemId) {
        Cursor cursor = dbHelper.readItem(itemId);
        cursor.moveToFirst();
        nameEditText.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        priceEditText.setText(cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)));
        quantityEditText.setText(cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY)));
        supplierNameEditText.setText(cursor.getString(cursor.getColumnIndex(COLUMN_SUPPLIER_NAME)));
        supplierPhoneEditText.setText(cursor.getString(cursor.getColumnIndex(COLUMN_SUPPLIER_PHONE)));
        supplierEmailEditText.setText(cursor.getString(cursor.getColumnIndex(COLUMN_SUPPLIER_EMAIL)));
        imageView.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))));
        nameEditText.setEnabled(false);
        priceEditText.setEnabled(false);
        supplierNameEditText.setEnabled(false);
        supplierPhoneEditText.setEnabled(false);
        supplierEmailEditText.setEnabled(false);
        imageButton.setEnabled(false);

    }
}
