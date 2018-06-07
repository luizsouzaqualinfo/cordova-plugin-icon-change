/**
 */
package br.com.qualinfo;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

public class IconChange extends CordovaPlugin {
  private static final String LOG_TAG = "QUALINFO";
  private final String PREF_LAST_ICON_NAME = "LAST_ICON_NAME";

  private String mCurrentIconName = null;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Log.d(LOG_TAG, "Inicializando Qualinfo App Icon Change");

  }

  @Override
  public boolean execute(String action, 
                        JSONArray args, 
                        final CallbackContext callbackContext)
                  throws JSONException {
    
    PluginResult result = null;

    if("getCurrent".equals(action)) {
        result = new PluginResult(PluginResult.Status.OK, getCurrentIconName());
    } else if ("change".equals(action)) {
        if(!getCurrentIconName().equals(args.getString(0))) {
            changeIcon(args.getString(0));
        }
        result = new PluginResult(PluginResult.Status.OK);
    }

    callbackContext.sendPluginResult(result);
    return true;
  }

  /**
   * @param ctx O contexto do Android
   * @return Retorna a inst&acirc;ncia que gerencia o arquivo de prefer&ecirc;ncias para o plugin
   */
  private SharedPreferences getPreferences(Context ctx) {
      return ctx.getSharedPreferences("AppIconChange", Context.MODE_PRIVATE);
  }

  /**
   * @return Retorna o nome do &uacute;ltimo &iacute;cone usado
   */
  private String getCurrentIconName() {
      if(mCurrentIconName == null) {
          mCurrentIconName = getPreferences(this.cordova.getActivity().getApplicationContext())
                  .getString(PREF_LAST_ICON_NAME, "MainActivity");
      }
        
      return mCurrentIconName;
  }

  /**
    * Muda o &iacute;cone do app, se e apenas se, o alias foi configurado
    *
    * @param newIconName
    */
  private void changeIcon(String newIconName) {
      Context c = this.cordova.getActivity().getApplicationContext();
      String packageName = c.getPackageName();

      try {
          // Ativando um icone
          c.getPackageManager()
                  .setComponentEnabledSetting(
                          new ComponentName(packageName, packageName + "." + newIconName),
                          PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                          PackageManager.DONT_KILL_APP);
          // Desativando o antigo
          c.getPackageManager()
                  .setComponentEnabledSetting(
                          new ComponentName(packageName, packageName + "." + getCurrentIconName()),
                          PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                          PackageManager.DONT_KILL_APP);

          // Se tudo ocorreu como esperado entao salva o novo nome
          mCurrentIconName = newIconName;
          getPreferences(c)
                  .edit()
                  .putString(PREF_LAST_ICON_NAME, newIconName)
                  .apply();

          new AlertDialog.Builder(this.cordova.getActivity())
                  .setMessage("A aplicativo será fechado em alguns segundos, após isso acesse novamente através do novo ícone")
                  .setCancelable(false)
                  .setNegativeButton("Fechar", null)
                  .show();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
}
