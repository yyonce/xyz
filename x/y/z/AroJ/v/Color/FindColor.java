package x.y.z.AroJ.v.Color;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.lang.Object;
import java.lang.Deprecated;

///
import x.y.z.AroJ.v.Color.Palette;


//7 3 24 (11 (12) 3 24)
public final class FindColor extends Palette
{ 
  final Builder BUILD;
  final Object PALETTE;


  FindColor(Builder z) {
    BUILD = z;
    PALETTE = null;
  }

  // <CP>
  FindColor(Bitmap v) {
    BUILD = null;
    PALETTE = androidx.palette.graphics.Palette.from(v).generate(); 
  }



  // TEST
  public static final FindColor initFor(Bitmap v, boolean k) { // k >> PALETTE PACKAGE 
    if (!k) {
      return new FindColor(new Builder(v));
    }
    return new FindColor(v);
  }


  public static final FindColor initFor(Bitmap v) {
    return initFor(v, false);
  }




  // <CORE>
  public final int toColor(int i, int k, boolean kk) { // kk >> DEFAULT is TRUE, 防止 Null 
    if (BUILD != null) {
      int ii = BUILD.toColor(i, k);
      if (ii == i && kk && k != TYPE_DEFAULT) {
        ii = BUILD.toColor(i, TYPE_DEFAULT);
      }
      return ii;
    }
    if (PALETTE != null) {
      final androidx.palette.graphics.Palette z = (androidx.palette.graphics.Palette) PALETTE;
      androidx.palette.graphics.Palette.Swatch v = null;
      if (k == TYPE_NOLIGHT) {
        v = v != null ? v : z.getDarkVibrantSwatch(); 
      } 
      if (k == TYPE_LIGHT) {
        v = v != null ? v : z.getLightVibrantSwatch(); 
      } 
      if (kk || k == TYPE_DEFAULT) {
        v = v != null ? v : z.getVibrantSwatch();
        v = v != null ? v : z.getMutedSwatch(); 
      }
      return v != null ? v.getRgb() : i;
    }
    return i;
  }


  public final int toColor(int i, int k) { 
    return toColor(i, k, k == TYPE_DEFAULT);
  }


  public final int toColor(int i) { // [1]
    return toColor(i, TYPE_DEFAULT); 
  }


  @Deprecated // > [1]
  public final int toColor() { // [0]
    return toColor(Color.TRANSPARENT); 
  }



}
