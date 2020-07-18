package vn.vistark.pharmass.component.barcode_scanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import android.widget.Toast
import com.google.android.gms.vision.barcode.Barcode
import info.androidhive.barcode.BarcodeReader
import vn.vistark.pharmass.R

class BarcodeScannerActivity : AppCompatActivity(), BarcodeReader.BarcodeReaderListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner)
    }

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>?) {
    }

    override fun onScannedMultiple(barcodes: MutableList<Barcode>?) {
        barcodes?.forEach {
            Toast.makeText(this, it.rawValue, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCameraPermissionDenied() {
        Toast.makeText(this, "Chưa cấp quyền truy cập máy ảnh", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onScanned(barcode: Barcode?) {
        if (barcode != null) {
            Toast.makeText(this, barcode.rawValue, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onScanError(errorMessage: String?) {
    }
}