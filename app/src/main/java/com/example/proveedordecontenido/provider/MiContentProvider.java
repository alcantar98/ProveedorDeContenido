package com.example.proveedordecontenido.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proveedordecontenido.Dao.BDProductos;

public class MiContentProvider extends ContentProvider {
    //Uri para obtener un solo elemento y todos los elementos

    // context://com.example.proveedordecontenido.provider.MiContentProvider/productos/#
    private static final int PRODUCTO_ONE_REG=1;
    // context://com.example.proveedordecontenido.provider.MiContentProvider/productos
    private static final int PRODUCTO_ALL_REG=2;

    private SQLiteDatabase bdProducto;
    public BDProductos baseProductos;
    //Nombre de nuestra base de datos
    private static final String DATABASE_NAME="bdProductos";
    //Version de nuestra base de datos
    private static final int DATABASE_VERSION=1;

    //Nombre de la tabla
    private static final String PRODUCTO_TABLE_NAME="productos";

    private static final UriMatcher sUriMatcher;
    private static final SparseArray<String> sMimeTypes;
    static {
        sUriMatcher=new UriMatcher(0);
        sMimeTypes=new SparseArray<String>();

        //Agregamos las dos Uri
        sUriMatcher.addURI(Contrato.AUTHORITY,PRODUCTO_TABLE_NAME,PRODUCTO_ALL_REG);
        sUriMatcher.addURI(Contrato.AUTHORITY,PRODUCTO_TABLE_NAME+"/#",PRODUCTO_ONE_REG);

        //Especificamos los tipos MIME
        //Para todos los registros
        sMimeTypes.put(PRODUCTO_ALL_REG, "vnd.android.cursor.dir/vnd."+Contrato.AUTHORITY+"."+PRODUCTO_TABLE_NAME);
        //Para un solo registros
        sMimeTypes.put(PRODUCTO_ONE_REG, "vnd.android.cursor.item/vnd."+Contrato.AUTHORITY+"."+PRODUCTO_TABLE_NAME);
    }


    @Override
    public boolean onCreate() {
        baseProductos=new BDProductos(getContext(),DATABASE_NAME,null,DATABASE_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder=new SQLiteQueryBuilder();
        bdProducto=baseProductos.getReadableDatabase();
        switch (sUriMatcher.match(uri)){
            case PRODUCTO_ONE_REG:
                if(selection==null)
                    selection="";
                selection+=Contrato.Producto.CODIGO+" = "+uri.getLastPathSegment();
                sqLiteQueryBuilder.setTables(PRODUCTO_TABLE_NAME);
                break;
            case PRODUCTO_ALL_REG:
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder=Contrato.Producto.CODIGO+" ASC";
                sqLiteQueryBuilder.setTables(PRODUCTO_TABLE_NAME);
                break;
        }
        Cursor cursor;
        cursor=sqLiteQueryBuilder.query(bdProducto,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)){
            case PRODUCTO_ALL_REG:
                return sMimeTypes.get(1);
            case PRODUCTO_ONE_REG:
                return sMimeTypes.get(2);
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        bdProducto=baseProductos.getWritableDatabase();
        String table="";
        switch (sUriMatcher.match(uri)){
            case PRODUCTO_ALL_REG:
                table=PRODUCTO_TABLE_NAME;
                break;
        }
        long rowId=bdProducto.insert(table,null, values);
        if(rowId>0){
            Uri rowUri= ContentUris.appendId(
                    uri.buildUpon(),
                    rowId
            ).build();
            getContext().getContentResolver().notifyChange(rowUri,null);
            return rowUri;
        }
        bdProducto.close();
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        bdProducto=baseProductos.getWritableDatabase();
        String table="";
        switch (sUriMatcher.match(uri)){
            case PRODUCTO_ONE_REG:
                if(selection==null){
                    selection="";
                }
                selection+=Contrato.Producto._ID+" = " + uri.getLastPathSegment();
                table=PRODUCTO_TABLE_NAME;
                break;
            case PRODUCTO_ALL_REG:
                table=PRODUCTO_TABLE_NAME;
                break;
        }
        int rows=bdProducto.delete(table,selection,selectionArgs);
        if (rows>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        bdProducto.close();
        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        bdProducto=baseProductos.getWritableDatabase();

        String table="";
        switch (sUriMatcher.match(uri)){
            case PRODUCTO_ONE_REG:
                if (selection==null)
                    selection="";
                selection+=Contrato.Producto._ID+" = "+uri.getLastPathSegment() ;
                table=PRODUCTO_TABLE_NAME;
                break;
            case PRODUCTO_ALL_REG:
                table=PRODUCTO_TABLE_NAME;
                break;
        }
        int rows=bdProducto.update(table,values,selection,selectionArgs);
        if(rows>0){
            getContext().getContentResolver().notifyChange(uri,null);

        }
        bdProducto.close();
        return rows;
    }
}
