$wnd.OERC_Dashboard_widgetset_OercWidgetset.runAsyncCallback6("function $clearBackgroundColor(this$static){\n  $clinit_Style();\n  $clearProperty(this$static, 'backgroundColor');\n}\n\ndefineClass(205, 81, {35:1, 1:1});\n_.getRelativeElement = function getRelativeElement(){\n  this.assertLive();\n  return this.relativeElem;\n}\n;\ndefineClass(206, 709, {35:1, 1:1});\n_.getX = function getX(){\n  var relativeElem;\n  relativeElem = this.getRelativeElement();\n  if (isNotNull(relativeElem)) {\n    return this.getRelativeX(relativeElem);\n  }\n  return this.getClientX();\n}\n;\n_.getY = function getY(){\n  var relativeElem;\n  relativeElem = this.getRelativeElement();\n  if (isNotNull(relativeElem)) {\n    return this.getRelativeY(relativeElem);\n  }\n  return this.getClientY();\n}\n;\ndefineClass(11, 1, {11:1, 1:1});\n_.setPixelSize = function setPixelSize(width_0, height){\n  if (width_0 >= 0) {\n    this.setWidth_0(width_0 + 'px');\n  }\n  if (height >= 0) {\n    this.setHeight_0(height + 'px');\n  }\n}\n;\nfunction $clinit_SourcesMouseWheelEvents(){\n  $clinit_SourcesMouseWheelEvents = emptyMethod;\n}\n\nvar Lcom_google_gwt_user_client_ui_SourcesMouseWheelEvents_2_classLit = createForInterface('com.google.gwt.user.client.ui', 'SourcesMouseWheelEvents');\nfunction FocusPanel(){\n  $clinit_FocusPanel();\n  SimplePanel_0.call(this, impl_11.createFocusable());\n  this.$init_398();\n}\n\ndefineClass(2189, 92, {77:1, 45:1, 42:1, 16:1, 55:1, 29:1, 11:1, 8:1, 19:1, 1:1});\n_.$init_398 = function $init_398(){\n}\n;\n_.addBlurHandler = function addBlurHandler_0(handler){\n  return this.addDomHandler(handler, getType_8());\n}\n;\n_.addClickHandler = function addClickHandler_1(handler){\n  return this.addDomHandler(handler, getType_10());\n}\n;\n_.addDoubleClickHandler = function addDoubleClickHandler_1(handler){\n  return this.addDomHandler(handler, getType_12());\n}\n;\n_.addFocusHandler = function addFocusHandler_0(handler){\n  return this.addDomHandler(handler, getType_13());\n}\n;\n_.addKeyUpHandler = function addKeyUpHandler_0(handler){\n  return this.addDomHandler(handler, getType_16());\n}\n;\n_.addMouseUpHandler = function addMouseUpHandler_0(handler){\n  return this.addDomHandler(handler, getType_22());\n}\n;\n_.setFocus = function setFocus_2(focused){\n  if (focused) {\n    impl_11.focus_1(this.getElement());\n  }\n   else {\n    impl_11.blur_2(this.getElement());\n  }\n}\n;\n_.setTabIndex = function setTabIndex_2(index_0){\n  impl_11.setTabIndex_0(this.getElement(), index_0);\n}\n;\ndefineClass(75, 418, {45:1, 42:1, 16:1, 75:1, 11:1, 8:1, 1:1});\n_.addMouseMoveHandler = function addMouseMoveHandler(handler){\n  return this.addDomHandler(handler, getType_19());\n}\n;\n_.addMouseUpHandler = function addMouseUpHandler_1(handler){\n  return this.addDomHandler(handler, getType_22());\n}\n;\ndefineClass(1608, 1, {62:1, 2892:1, 3:1, 1:1});\n_.select_6 = function select_11(p0, p1){\n  this.val$handler2.invoke(this, getType_38(Lcom_vaadin_shared_ui_colorpicker_ColorPickerGradientServerRpc_2_classLit).getMethod('select'), stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_Object_2_classLit, 1), {3:1, 1:1, 5:1}, 1, 5, [valueOf_68(p0), valueOf_68(p1)]));\n}\n;\ndefineClass(1726, 1, {203:1, 1:1});\n_.load_8 = function load_11(){\n  this.val$store2.setSuperClass(Lcom_vaadin_shared_ui_colorpicker_ColorPickerGradientState_2_classLit, Lcom_vaadin_shared_AbstractFieldState_2_classLit);\n  this.val$store2.setClass('com.vaadin.ui.components.colorpicker.ColorPickerGradient', Lcom_vaadin_client_ui_colorpicker_ColorPickerGradientConnector_2_classLit);\n  this.val$store2.setConstructor(Lcom_vaadin_client_ui_colorpicker_ColorPickerGradientConnector_2_classLit, new ConnectorBundleLoaderImpl$6$1$1(this));\n  this.val$store2.setConstructor(Lcom_vaadin_shared_ui_colorpicker_ColorPickerGradientState_2_classLit, new ConnectorBundleLoaderImpl$6$1$2(this));\n  this.val$store2.setReturnType(Lcom_vaadin_client_ui_colorpicker_ColorPickerGradientConnector_2_classLit, 'getState', new Type(Lcom_vaadin_shared_ui_colorpicker_ColorPickerGradientState_2_classLit));\n  this.val$store2.setInvoker(Lcom_vaadin_client_ui_colorpicker_ColorPickerGradientConnector_2_classLit, 'updateBgColor', new ConnectorBundleLoaderImpl$6$1$3(this));\n  this.val$store2.setInvoker(Lcom_vaadin_client_ui_colorpicker_ColorPickerGradientConnector_2_classLit, 'updateCursor', new ConnectorBundleLoaderImpl$6$1$4(this));\n  this.loadJsBundle_4(this.val$store2);\n  this.val$store2.setPropertyType(Lcom_vaadin_shared_ui_colorpicker_ColorPickerGradientState_2_classLit, 'bgColor', new Type(Ljava_lang_String_2_classLit));\n  this.val$store2.setPropertyType(Lcom_vaadin_shared_ui_colorpicker_ColorPickerGradientState_2_classLit, 'cursorX', new Type(Ljava_lang_Integer_2_classLit));\n  this.val$store2.setPropertyType(Lcom_vaadin_shared_ui_colorpicker_ColorPickerGradientState_2_classLit, 'cursorY', new Type(Ljava_lang_Integer_2_classLit));\n  this.val$store2.addOnStateChangeMethod(Lcom_vaadin_client_ui_colorpicker_ColorPickerGradientConnector_2_classLit, new OnStateChangeMethod(Lcom_vaadin_client_ui_AbstractComponentConnector_2_classLit, 'handleContextClickListenerChange', stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), {3:1, 1:1, 5:1, 15:1}, 2, 6, ['registeredEventListeners'])));\n  this.val$store2.addOnStateChangeMethod(Lcom_vaadin_client_ui_colorpicker_ColorPickerGradientConnector_2_classLit, new OnStateChangeMethod_0('updateBgColor', stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), {3:1, 1:1, 5:1, 15:1}, 2, 6, ['bgColor'])));\n  this.val$store2.addOnStateChangeMethod(Lcom_vaadin_client_ui_colorpicker_ColorPickerGradientConnector_2_classLit, new OnStateChangeMethod_0('updateCursor', stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), {3:1, 1:1, 5:1, 15:1}, 2, 6, ['cursorX', 'cursorY'])));\n}\n;\n_.loadJsBundle_4 = function loadJsBundle_4(store){\n  this.loadNativeJs_4(store);\n}\n;\n_.loadNativeJs_4 = function loadNativeJs_4(store){\n  var data_0 = {setter:function(bean, value_0){\n    bean.bgColor = value_0;\n  }\n  , getter:function(bean){\n    return bean.bgColor;\n  }\n  };\n  store.setPropertyData(Lcom_vaadin_shared_ui_colorpicker_ColorPickerGradientState_2_classLit, 'bgColor', data_0);\n  var data_0 = {setter:function(bean, value_0){\n    bean.cursorX = value_0.intValue();\n  }\n  , getter:function(bean){\n    return valueOf_68(bean.cursorX);\n  }\n  };\n  store.setPropertyData(Lcom_vaadin_shared_ui_colorpicker_ColorPickerGradientState_2_classLit, 'cursorX', data_0);\n  var data_0 = {setter:function(bean, value_0){\n    bean.cursorY = value_0.intValue();\n  }\n  , getter:function(bean){\n    return valueOf_68(bean.cursorY);\n  }\n  };\n  store.setPropertyData(Lcom_vaadin_shared_ui_colorpicker_ColorPickerGradientState_2_classLit, 'cursorY', data_0);\n}\n;\n_.onSuccess_1 = function onSuccess_10(){\n  this.load_8();\n  get_26().setLoaded_0(this.this$11.getName());\n}\n;\nfunction $clinit_ConnectorBundleLoaderImpl$6$1$1(){\n  $clinit_ConnectorBundleLoaderImpl$6$1$1 = emptyMethod;\n  $clinit_Object();\n}\n\nfunction ConnectorBundleLoaderImpl$6$1$1(this$2){\n  $clinit_ConnectorBundleLoaderImpl$6$1$1();\n  this.this$21 = this$2;\n  Object_0.call(this);\n  this.$init_1212();\n}\n\ndefineClass(1727, 1, {6:1, 1:1}, ConnectorBundleLoaderImpl$6$1$1);\n_.$init_1212 = function $init_1212(){\n}\n;\n_.invoke_0 = function invoke_298(target, params){\n  return new ColorPickerGradientConnector;\n}\n;\nvar Lcom_vaadin_client_metadata_ConnectorBundleLoaderImpl$6$1$1_2_classLit = createForClass('com.vaadin.client.metadata', 'ConnectorBundleLoaderImpl/6/1/1', 1727, Ljava_lang_Object_2_classLit);\nfunction $clinit_ConnectorBundleLoaderImpl$6$1$2(){\n  $clinit_ConnectorBundleLoaderImpl$6$1$2 = emptyMethod;\n  $clinit_Object();\n}\n\nfunction ConnectorBundleLoaderImpl$6$1$2(this$2){\n  $clinit_ConnectorBundleLoaderImpl$6$1$2();\n  this.this$21 = this$2;\n  Object_0.call(this);\n  this.$init_1213();\n}\n\ndefineClass(1728, 1, {6:1, 1:1}, ConnectorBundleLoaderImpl$6$1$2);\n_.$init_1213 = function $init_1213(){\n}\n;\n_.invoke_0 = function invoke_299(target, params){\n  return new ColorPickerGradientState;\n}\n;\nvar Lcom_vaadin_client_metadata_ConnectorBundleLoaderImpl$6$1$2_2_classLit = createForClass('com.vaadin.client.metadata', 'ConnectorBundleLoaderImpl/6/1/2', 1728, Ljava_lang_Object_2_classLit);\nfunction $clinit_ConnectorBundleLoaderImpl$6$1$3(){\n  $clinit_ConnectorBundleLoaderImpl$6$1$3 = emptyMethod;\n  $clinit_JsniInvoker();\n}\n\nfunction ConnectorBundleLoaderImpl$6$1$3(this$2){\n  $clinit_ConnectorBundleLoaderImpl$6$1$3();\n  this.this$21 = this$2;\n  JsniInvoker.call(this);\n  this.$init_1214();\n}\n\ndefineClass(1729, 46, {6:1, 1:1}, ConnectorBundleLoaderImpl$6$1$3);\n_.$init_1214 = function $init_1214(){\n}\n;\n_.jsniInvoke = function jsniInvoke_48(target, params){\n  target.updateBgColor();\n  return null;\n}\n;\nvar Lcom_vaadin_client_metadata_ConnectorBundleLoaderImpl$6$1$3_2_classLit = createForClass('com.vaadin.client.metadata', 'ConnectorBundleLoaderImpl/6/1/3', 1729, Lcom_vaadin_client_metadata_JsniInvoker_2_classLit);\nfunction $clinit_ConnectorBundleLoaderImpl$6$1$4(){\n  $clinit_ConnectorBundleLoaderImpl$6$1$4 = emptyMethod;\n  $clinit_JsniInvoker();\n}\n\nfunction ConnectorBundleLoaderImpl$6$1$4(this$2){\n  $clinit_ConnectorBundleLoaderImpl$6$1$4();\n  this.this$21 = this$2;\n  JsniInvoker.call(this);\n  this.$init_1215();\n}\n\ndefineClass(1730, 46, {6:1, 1:1}, ConnectorBundleLoaderImpl$6$1$4);\n_.$init_1215 = function $init_1215(){\n}\n;\n_.jsniInvoke = function jsniInvoke_49(target, params){\n  target.updateCursor_0();\n  return null;\n}\n;\nvar Lcom_vaadin_client_metadata_ConnectorBundleLoaderImpl$6$1$4_2_classLit = createForClass('com.vaadin.client.metadata', 'ConnectorBundleLoaderImpl/6/1/4', 1730, Lcom_vaadin_client_metadata_JsniInvoker_2_classLit);\nfunction $clinit_ColorPickerGradientConnector(){\n  $clinit_ColorPickerGradientConnector = emptyMethod;\n  $clinit_AbstractComponentConnector();\n}\n\nfunction ColorPickerGradientConnector(){\n  $clinit_ColorPickerGradientConnector();\n  AbstractComponentConnector.call(this);\n  this.$init_1449();\n}\n\ndefineClass(2171, 13, {106:1, 7:1, 14:1, 22:1, 26:1, 13:1, 17:1, 33:1, 24:1, 3:1, 1:1}, ColorPickerGradientConnector);\n_.$init_1449 = function $init_1449(){\n  this.rpc = castTo(create_12(Lcom_vaadin_shared_ui_colorpicker_ColorPickerGradientServerRpc_2_classLit, this), 2892);\n}\n;\n_.getState = function getState_121(){\n  return this.getState_41();\n}\n;\n_.getState_0 = function getState_122(){\n  return this.getState_41();\n}\n;\n_.getWidget_0 = function getWidget_38(){\n  return this.getWidget_18();\n}\n;\n_.createWidget = function createWidget_10(){\n  return castTo(new VColorPickerGradient, 8);\n}\n;\n_.getState_41 = function getState_123(){\n  return castTo(getClassPrototype(13).getState.call(this), 652);\n}\n;\n_.getWidget_18 = function getWidget_39(){\n  return castTo(getClassPrototype(13).getWidget_0.call(this), 665);\n}\n;\n_.init_3 = function init_30(){\n  getClassPrototype(17).init_3.call(this);\n  this.getWidget_18().addMouseUpHandler(this);\n}\n;\n_.onMouseUp = function onMouseUp_3(event_0){\n  this.rpc.select_6(this.getWidget_18().getCursorX(), this.getWidget_18().getCursorY());\n}\n;\n_.updateBgColor = function updateBgColor(){\n  this.getWidget_18().setBGColor(this.getState_41().bgColor);\n}\n;\n_.updateCursor_0 = function updateCursor_0(){\n  this.getWidget_18().setCursor(this.getState_41().cursorX, this.getState_41().cursorY);\n}\n;\nvar Lcom_vaadin_client_ui_colorpicker_ColorPickerGradientConnector_2_classLit = createForClass('com.vaadin.client.ui.colorpicker', 'ColorPickerGradientConnector', 2171, Lcom_vaadin_client_ui_AbstractComponentConnector_2_classLit);\nfunction $clinit_VColorPickerGradient(){\n  $clinit_VColorPickerGradient = emptyMethod;\n  $clinit_FocusPanel();\n  CLASSNAME_BACKGROUND = 'v-colorpicker-gradient' + '-background';\n  CLASSNAME_FOREGROUND = 'v-colorpicker-gradient' + '-foreground';\n  CLASSNAME_LOWERBOX = 'v-colorpicker-gradient' + '-lowerbox';\n  CLASSNAME_HIGHERBOX = 'v-colorpicker-gradient' + '-higherbox';\n  CLASSNAME_CONTAINER = 'v-colorpicker-gradient' + '-container';\n  CLASSNAME_CLICKLAYER = 'v-colorpicker-gradient' + '-clicklayer';\n}\n\nfunction VColorPickerGradient(){\n  $clinit_VColorPickerGradient();\n  FocusPanel.call(this);\n  this.$init_1451();\n  this.setStyleName('v-colorpicker-gradient');\n  this.background = new HTML;\n  this.background.setStyleName('v-colorpicker-gradient-background');\n  this.background.setPixelSize(this.width_0, this.height_0);\n  this.foreground = new HTML;\n  this.foreground.setStyleName('v-colorpicker-gradient-foreground');\n  this.foreground.setPixelSize(this.width_0, this.height_0);\n  this.clicklayer = new HTML;\n  this.clicklayer.setStyleName('v-colorpicker-gradient-clicklayer');\n  this.clicklayer.setPixelSize(this.width_0, this.height_0);\n  this.clicklayer.addMouseDownHandler(this);\n  this.clicklayer.addMouseUpHandler(this);\n  this.clicklayer.addMouseMoveHandler(this);\n  this.lowercross = new HTML;\n  this.lowercross.setPixelSize(narrow_int(this.width_0 / 2), narrow_int(this.height_0 / 2));\n  this.lowercross.setStyleName('v-colorpicker-gradient-lowerbox');\n  this.highercross = new HTML;\n  this.highercross.setPixelSize(narrow_int(this.width_0 / 2), narrow_int(this.height_0 / 2));\n  this.highercross.setStyleName('v-colorpicker-gradient-higherbox');\n  this.container = new AbsolutePanel;\n  this.container.setStyleName('v-colorpicker-gradient-container');\n  this.container.setPixelSize(this.width_0, this.height_0);\n  this.container.add_4(this.background, 0, 0);\n  this.container.add_4(this.foreground, 0, 0);\n  this.container.add_4(this.lowercross, 0, narrow_int(this.height_0 / 2));\n  this.container.add_4(this.highercross, narrow_int(this.width_0 / 2), 0);\n  this.container.add_4(this.clicklayer, 0, 0);\n  this.add_1(this.container);\n}\n\ndefineClass(665, 2189, {77:1, 45:1, 42:1, 95:1, 678:1, 106:1, 7:1, 16:1, 55:1, 29:1, 11:1, 8:1, 97:1, 665:1, 19:1, 1:1}, VColorPickerGradient);\n_.$init_1451 = function $init_1451(){\n  this.mouseIsDown = false;\n  this.width_0 = 220;\n  this.height_0 = 220;\n}\n;\n_.getCursorX = function getCursorX(){\n  return this.cursorX;\n}\n;\n_.getCursorY = function getCursorY(){\n  return this.cursorY;\n}\n;\n_.getSubPartElement = function getSubPartElement_10(subPart){\n  if (equals_Ljava_lang_Object__Z__devirtual$_1(subPart, 'clicklayer')) {\n    return this.clicklayer.getElement();\n  }\n  return null;\n}\n;\n_.getSubPartName = function getSubPartName_10(subElement){\n  if ($isOrHasChild(this.clicklayer.getElement(), subElement)) {\n    return 'clicklayer';\n  }\n  return null;\n}\n;\n_.onMouseDown = function onMouseDown_8(event_0){\n  event_0.preventDefault_0();\n  this.mouseIsDown = true;\n  this.setCursor(event_0.getX(), event_0.getY());\n}\n;\n_.onMouseMove = function onMouseMove_2(event_0){\n  event_0.preventDefault_0();\n  if (this.mouseIsDown) {\n    this.setCursor(event_0.getX(), event_0.getY());\n  }\n}\n;\n_.onMouseUp = function onMouseUp_4(event_0){\n  event_0.preventDefault_0();\n  this.mouseIsDown = false;\n  this.setCursor(event_0.getX(), event_0.getY());\n  this.cursorX = event_0.getX();\n  this.cursorY = event_0.getY();\n}\n;\n_.setBGColor = function setBGColor(bgColor){\n  if (jsEquals(bgColor, null)) {\n    $clearBackgroundColor($getStyle(this.background.getElement()));\n  }\n   else {\n    $setBackgroundColor($getStyle(this.background.getElement()), bgColor);\n  }\n}\n;\n_.setCursor = function setCursor(x_0, y_0){\n  this.cursorX = x_0;\n  this.cursorY = y_0;\n  if (x_0 >= 0) {\n    $setWidth_0($getStyle(this.lowercross.getElement()), x_0, ($clinit_Style$Unit() , PX));\n  }\n  if (y_0 >= 0) {\n    $setTop($getStyle(this.lowercross.getElement()), y_0, ($clinit_Style$Unit() , PX));\n  }\n  if (y_0 >= 0) {\n    $setHeight_0($getStyle(this.lowercross.getElement()), this.height_0 - y_0, ($clinit_Style$Unit() , PX));\n  }\n  if (x_0 >= 0) {\n    $setWidth_0($getStyle(this.highercross.getElement()), this.width_0 - x_0, ($clinit_Style$Unit() , PX));\n  }\n  if (x_0 >= 0) {\n    $setLeft($getStyle(this.highercross.getElement()), x_0, ($clinit_Style$Unit() , PX));\n  }\n  if (y_0 >= 0) {\n    $setHeight_0($getStyle(this.highercross.getElement()), y_0, ($clinit_Style$Unit() , PX));\n  }\n}\n;\n_.cursorX = 0;\n_.cursorY = 0;\n_.height_0 = 0;\n_.mouseIsDown = false;\n_.width_0 = 0;\nvar CLASSNAME_BACKGROUND, CLASSNAME_CLICKLAYER, CLASSNAME_CONTAINER, CLASSNAME_FOREGROUND, CLASSNAME_HIGHERBOX, CLASSNAME_LOWERBOX;\nvar Lcom_vaadin_client_ui_colorpicker_VColorPickerGradient_2_classLit = createForClass('com.vaadin.client.ui.colorpicker', 'VColorPickerGradient', 665, Lcom_google_gwt_user_client_ui_FocusPanel_2_classLit);\nfunction $clinit_ColorPickerGradientState(){\n  $clinit_ColorPickerGradientState = emptyMethod;\n  $clinit_AbstractFieldState();\n}\n\nfunction ColorPickerGradientState(){\n  $clinit_ColorPickerGradientState();\n  AbstractFieldState.call(this);\n  this.$init_1839();\n}\n\ndefineClass(652, 59, {32:1, 59:1, 28:1, 65:1, 652:1, 3:1, 1:1}, ColorPickerGradientState);\n_.$init_1839 = function $init_1839(){\n}\n;\n_.cursorX = 0;\n_.cursorY = 0;\nvar Lcom_vaadin_shared_ui_colorpicker_ColorPickerGradientState_2_classLit = createForClass('com.vaadin.shared.ui.colorpicker', 'ColorPickerGradientState', 652, Lcom_vaadin_shared_AbstractFieldState_2_classLit);\n$entry(onLoad)(6);\n\n//# sourceURL=OERC.Dashboard.widgetset.OercWidgetset-6.js\n")
