/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 *
 * Copyright (c) 2008, CILEA and third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by CILEA.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License v3 or any later version, as published 
 * by the Free Software Foundation, Inc. <http://fsf.org/>.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
function changeArea(areaId) {
	var element = document.getElementById('newTabId');
	element.setAttribute('value', areaId);
	document.getElementById('anagraficadto').submit();
}

function showHelp(helpId) {
	var helpTip = document.getElementById(helpId);
	helpTip.style.display = 'block';
}

function hideHelp(helpId) {
	var helpTip = document.getElementById(helpId);
	helpTip.style.display = 'none';
}

// popola la select per le classificazioni
function makeSelection(inputId, labelId, selectId, treeName, idxSelectedItem) {

	var element = document.getElementById(selectId);
	// nella tendina delle classificazioni scelgo quello con indice =
	// indiceClassificazioneScelta e prendo il testo associato all'option della
	// select
	// che mi servir� per aggiornare la label
	var selectedElement = element.options[idxSelectedItem].text;

	var element2 = document.getElementById(labelId);
	var element3 = document.getElementById(inputId);
	element2.innerText = element2.textContent = selectedElement; // innerText serve ad IE...
	element3.value = element.value;
	var select = element.value;
	dwr.util.removeAllOptions(element);
	AjaxService.getClassificazioni(treeName, select, function(options) {
		gotMakeSelection(options, inputId, labelId, selectId, selectedElement);
	});
}

// callback
function gotMakeSelection(options, inputId, labelId, selectId,
		selectedElementText) {
	var element2 = document.getElementById(labelId);
	// element2.innerText = element2.textContent += " - " + selectedElementText;
	// //innerText serve ad IE...
	element2.innerText = selectedElementText; // innerText serve ad IE...
	if (options.size() != 0) {
		dwr.util.addOptions(selectId, [ "...scegli opzione..." ]);
		dwr.util.addOptions(selectId, options, "identifyingValue",
				"displayValue");
	} else {
		dropdown = document.getElementById(selectId);
		dropdown.hide();
	}

}

// ripopola la select delle classificazione
function reloadSelect(inputId, labelId, selectId, treeName) {
	AjaxService.getClassificazioni(treeName, '', function(options) {
		gotReloadSelect(options, inputId, labelId, selectId)
	});
}

// callback
function gotReloadSelect(options, inputId, labelId, selectId) {
	element2 = document.getElementById(labelId);
	element2.textContent = "";
	element3 = document.getElementById(inputId);
	element3.value = "";
	dropdown = document.getElementById(selectId);
	dropdown.show();
	dwr.util.removeAllOptions(selectId);
	dwr.util.addOptions(selectId, [ "...scegli opzione..." ]);
	dwr.util.addOptions(selectId, options, "identifyingValue", "displayValue");
}

function clearSubjectChoise(idsuggestbox, inputName) {
	document.getElementById(idsuggestbox).value = "";
	document.getElementById(inputName).value = "";
}

function dynaSubjectUpdateValue(suggestbox, inputName, idsubjectlist) {
	soggettario = document.getElementById(idsubjectlist).value;
	if (soggettario != '') {
		document.getElementById(inputName).value = soggettario + ':'
				+ document.getElementById(suggestbox).value;
	}
}

function changeBooleanValue(idBoolean) {
	element = document.getElementById(idBoolean);
	if (element.value == null || element.value == 'false'
			|| element.value == '')
		element.value = 'true';
	else
		element.value = 'false';
}

function validateAnagraficaProperties(inputName, clazz, shortname) {
	// nothing
}

// callback
function setInputFieldErrorStatus(elementId, message) {
	var id = "error" + elementId;
	document.getElementById(id).style.display = "inline";
	document.getElementById(id).innerHTML = message;

}

// dynamic client side add/remove of properties

// helper functions
function insertAfter(referenceNode, newNode) {
	jQuery(newNode).insertAfter(jQuery(referenceNode));
}
function moveAfter(referenceNode, nodeToMove) {
	jQuery(nodeToMove).insertAfter(jQuery(referenceNode));
}

function createChildElement(parent, nodeType) {
	var child = document.createElement(nodeType);
	jQuery(child).appendTo(jQuery(parent));
	return child;
}

function createChildElementAfter(after, nodeType) {
	var child = document.createElement(nodeType);
	insertAfter(after, child);
	return child;
}

function removeElement(elementToDelete) {
	jQuery(elementToDelete).remove();
}

function createSpanError(after, propertyPath) {
	var errorSpan = createChildElementAfter(after, 'span');
	jQuery(errorSpan).addClass('fieldError');
	errorSpan.setAttribute('style', 'display: none;');
	errorSpan.setAttribute('id', 'error' + propertyPath);
	// createChildElement(errorSpan,'img');
	return errorSpan;
}

function deleteSpanError(inputName) {
	var errorid = 'error' + inputName;
	var error = document.getElementById(errorid);
	removeElement(error);
}

function createRemoveImgAfter(afterElement, jsvar, srcImg, idx) {
	var imgElement = createChildElementAfter(afterElement, 'img');
	jQuery(imgElement).addClass('deleteButton');
	jQuery(imgElement).bind('click', {
		iddelete : idx,
		jsobj : jsvar
	}, function(event) {
		eval(event.data.jsobj + '.remove(' + event.data.iddelete + ', this)');
	});
	imgElement.setAttribute('src', srcImg);
	// //move add button after span (afterElement)...
	var addButton = jQuery(afterElement).prev().get(0);
	moveAfter(jQuery(imgElement).next().get(0), afterElement);

	return imgElement;
}

// add functions
// TODO refactorin della classe in DynaTextInput
function AddTextInput(contextRoot, jsvar, propertyPath, startIdx, onchange,
		size, cssClass) {
	this.contextRoot = contextRoot;
	this.jsvar = jsvar;
	this.idx = startIdx;
	this.size = size;
	this.onchange = onchange;
	this.propertyPath = propertyPath;

	this.create = function() {
		var name = this.propertyPath + '[' + this.idx + ']';
		var afterElement = document.getElementById(
				'error' + this.propertyPath + '[' + (this.idx - 1) + ']')
				.previous();
		var removeImg = createRemoveImgAfter(afterElement, this.jsvar,
				this.contextRoot + '/image/jdyna/delete_icon.gif', this.idx - 1);
		var newLine = createChildElementAfter(removeImg.next().get(0), 'br');
		var inputValue = createChildElementAfter(newLine, 'input');

		if (this.idx == 0) { // rimuovo l'img delete per il primo elemento
			removeImg.parentNode.removeChild(removeImg);
		}

		inputValue.setAttribute('id', name);
		inputValue.setAttribute('name', name);
		inputValue.setAttribute('type', 'text');
		inputValue.setAttribute('onchange', this.onchange);
		inputValue.setAttribute('size', this.size);
		if (cssClass != '') {
			jQuery(inputValue).addClass(cssClass);
		}
		// var inputHidden =
		// createChildElementAfter(parentElement,afterElement,'input');
		createSpanError(inputValue.next().get(0), name);
		this.idx++;
	}

	this.remove = function(deleteId, buttonImg) {
		var name = this.propertyPath + '[' + deleteId + ']';
		var inputValue = document.getElementById(name);
		removeElement(inputValue);
		removeElement(jQuery(buttonImg).next().next().get(0)); // br
		removeElement(buttonImg);
		deleteSpanError(name);
	}
}

function AddTextInputWithVisibility(contextRoot, jsvar, propertyPath, startIdx,
		onchange, size, cssClass, visibility) {
	this.contextRoot = contextRoot;
	this.jsvar = jsvar;
	this.idx = startIdx;
	this.size = size;
	this.onchange = onchange;
	this.propertyPath = propertyPath;
	this.visibility = visibility;

	this.create = function() {
		var name = this.propertyPath + '[' + this.idx + ']';
		var afterElement = jQuery(
				document.getElementById('error' + this.propertyPath + '['
						+ (this.idx - 1) + ']')).prev().get(0);
		var removeImg = createRemoveImgAfter(afterElement, this.jsvar,
				this.contextRoot + '/image/jdyna/delete_icon.gif', this.idx - 1);
		var newLine = createChildElementAfter(jQuery(removeImg).next().get(0),
				'br');
		var inputValue = createChildElementAfter(newLine, 'input');

		if (this.idx == 0) { // rimuovo l'img delete per il primo elemento
			jQuery(removeImg).remove();
		}

		inputValue.setAttribute('id', name);
		inputValue.setAttribute('name', name);
		inputValue.setAttribute('type', 'text');
		inputValue.setAttribute('onchange', this.onchange);
		inputValue.setAttribute('size', this.size);

		if (cssClass != '') {
			jQuery(inputValue).addClass(cssClass);
		}
		// var inputHidden =
		// createChildElementAfter(parentElement,afterElement,'input');
		createSpanError(jQuery(inputValue).next().get(0), name);

		if (this.visibility == 'true') {
			createVisibility(name, inputValue);
		}

		this.idx++;

	}

	this.remove = function(deleteId, buttonImg) {
		var name = this.propertyPath + '[' + deleteId + ']';
		var inputValue = document.getElementById(name);
		if (this.visibility == 'true') {
			var checkbox = 'check' + name + '.visibility';
			var checkboxValue = document.getElementById(checkbox);
			removeElement(checkboxValue);
		}
		removeElement(inputValue);
		removeElement(jQuery(buttonImg).next().next().get(0)); // br
		removeElement(buttonImg);
		deleteSpanError(name);
	}
}

function createVisibility(name, afterElement) {
	var name_vis = name + '.visibility';
	var id_vis = 'check' + name_vis;
	var inputValue_vis = document.createElement('input');
	inputValue_vis.setAttribute('id', id_vis);
	inputValue_vis.setAttribute('name', name_vis);
	inputValue_vis.setAttribute('type', 'checkbox');
	inputValue_vis.setAttribute('value', 'true');
	jQuery(inputValue_vis).bind('click', {
		idbool : id_vis
	}, function(event) {
		cambiaBoolean(event.data.idbool);
	});
	jQuery(inputValue_vis).insertAfter(jQuery(afterElement));
	inputValue_vis.checked = 'checked';

	var inputValue_vis_hidden = document.createElement('input');
	inputValue_vis_hidden.setAttribute('id', "_" + id_vis);
	inputValue_vis_hidden.setAttribute('name', "_" + name_vis);
	inputValue_vis_hidden.setAttribute('type', 'hidden');
	inputValue_vis_hidden.setAttribute('value', 'true');
	jQuery(inputValue_vis_hidden).insertAfter(jQuery(inputValue_vis));
	return inputValue_vis;
}

function DynaTextAreaInput(contextRoot, jsvar, propertyPath, startIdx, cols,
		rows, toolbar) {
	this.contextRoot = contextRoot;
	this.jsvar = jsvar;
	this.idx = startIdx;
	this.cols = cols;
	this.rows = rows;
	this.toolbar = toolbar;
	this.propertyPath = propertyPath;

	this.create = function() {
		var name = this.propertyPath + '[' + this.idx + ']';
		var afterElement = jQuery(
				document.getElementById('error' + this.propertyPath + '['
						+ (this.idx - 1) + ']')).prev().get(0);
		var removeImg = createRemoveImgAfter(afterElement, this.jsvar,
				this.contextRoot + '/image/jdyna/delete_icon.gif', this.idx - 1);
		var newLine = createChildElementAfter(jQuery(removeImg).next().get(0),
				'br');
		var inputValue = createChildElementAfter(newLine, 'textarea');

		if (this.idx == 0) { // rimuovo l'img delete per il primo elemento
			removeElement(removeImg);
		}

		inputValue.setAttribute('id', name);
		inputValue.setAttribute('name', name);
		inputValue.setAttribute('rows', this.rows);
		inputValue.setAttribute('cols', this.cols);

		createSpanError(jQuery(inputValue).next().get(0), name);
		this.idx++;
		if (this.toolbar && this.toolbar != 'nessuna') {
			var oFCKeditor = new FCKeditor(name);
			oFCKeditor.BasePath = this.contextRoot + '/fckeditor/';
			oFCKeditor.ToolbarSet = this.toolbar;
			oFCKeditor.ReplaceTextarea();
		}
	}

	this.remove = function(deleteId, buttonImg) {
		var name = this.propertyPath + '[' + deleteId + ']';
		if (this.toolbar && this.toolbar != 'nessuna') {
			var fckeditor = FCKeditorAPI.GetInstance(name).EditorWindow.parent.frameElement;
			removeElement(fckeditor);
		}
		var inputValue = document.getElementById(name);
		removeElement(inputValue);
		removeElement(jQuery(buttonImg).next().next().get(0)); // br
		removeElement(buttonImg);
		deleteSpanError(name);
	}
}

function DynaDateInput(contextRoot, jsvar, propertyPath, startIdx, istime) {
	this.contextRoot = contextRoot;
	this.jsvar = jsvar;
	this.propertyPath = propertyPath;
	this.idx = startIdx;
	this.istime = istime;

	this.create = function() {
		var name = this.propertyPath + '[' + this.idx + ']';
		var afterElement = jQuery(
				document.getElementById('error' + this.propertyPath + '['
						+ (this.idx - 1) + ']')).prev().get(0);
		var removeImg = createRemoveImgAfter(afterElement, this.jsvar,
				this.contextRoot + '/image/jdyna/delete_icon.gif', this.idx - 1);
		var newLine = createChildElementAfter(jQuery(removeImg).next().get(0),
				'br');
		var inputValue = document.createElement('input');

		if (this.idx == 0) { // rimuovo l'img delete per il primo elemento
			removeElement(removeImg);
		}

		inputValue.setAttribute('id', name);
		inputValue.setAttribute('name', name);
		inputValue.setAttribute('type', 'text');
		jQuery(inputValue).insertAfter(jQuery(newLine));

		var calendarImg = createChildElementAfter(inputValue, 'img');
		calendarImg.setAttribute('id', 'calendar' + name);
		calendarImg.setAttribute('src', this.contextRoot
				+ '/image/jdyna/calendar.png');
		calendarImg.setAttribute('alt', 'calendar');
		jQuery(calendarImg).addClass('calendar');

		if (this.istime) {
			Calendar.setup({
				inputField : name, // ID of the input field
				ifFormat : "%d-%m-%Y %H:%M", // the date format
				button : "calendar" + name, // ID of the button
				cache : false,
				showsTime : true
			// show time as well as date
			});
		} else {
			Calendar.setup({
				inputField : name, // ID of the input field
				ifFormat : "%d-%m-%Y", // the date format
				button : "calendar" + name, // ID of the button
				cache : false,
				showsTime : false
			// show time as well as date
			});
		}

		createSpanError(jQuery(calendarImg).next().get(0), name);
		this.idx++;
	}

	this.remove = function(deleteId, buttonImg) {
		var name = this.propertyPath + '[' + deleteId + ']';
		var inputValue = document.getElementById(name);
		var calendarImg = document.getElementById('calendar' + name);
		removeElement(inputValue);
		removeElement(calendarImg);
		removeElement(jQuery(buttonImg).next().next().get(0)); // br
		removeElement(buttonImg);
		deleteSpanError(name);
	}
}

function DynaDateInputWithVisibility(contextRoot, jsvar, propertyPath,
		startIdx, istime, visibility, dateMin, dateMax) {
	this.contextRoot = contextRoot;
	this.jsvar = jsvar;
	this.propertyPath = propertyPath;
	this.idx = startIdx;
	this.istime = istime;
	this.visibility = visibility;

	this.create = function() {
		var name = this.propertyPath + '[' + this.idx + ']';
		var afterElement = jQuery(
				document.getElementById('error' + this.propertyPath + '['
						+ (this.idx - 1) + ']')).prev().get(0);
		var removeImg = createRemoveImgAfter(afterElement, this.jsvar,
				this.contextRoot + '/image/jdyna/delete_icon.gif', this.idx - 1);
		var newLine = createChildElementAfter(jQuery(removeImg).next().get(0),
				'br');
		var inputValue = document.createElement('input');

		if (this.idx == 0) { // rimuovo l'img delete per il primo elemento
			jQuery(removeImg).remove();
		}

		inputValue.setAttribute('id', name);
		inputValue.setAttribute('name', name);
		inputValue.setAttribute('type', 'text');
		inputValue.setAttribute('size', '10');
		jQuery(inputValue).insertAfter(jQuery(newLine));

		var calendarImg = createChildElementAfter(inputValue, 'img');
		calendarImg.setAttribute('id', 'calendar' + name);
		calendarImg.setAttribute('src', this.contextRoot
				+ '/image/jdyna/calendar.png');
		calendarImg.setAttribute('alt', 'calendar');
		jQuery(calendarImg).addClass('calendar');

		if (this.istime) {
			Calendar.setup({
				range: [dateMin, dateMax],
				inputField : name, // ID of the input field
				ifFormat : "%d-%m-%Y %H:%M", // the date format
				button : "calendar" + name, // ID of the button
				cache : false,
				showsTime : true
			// show time as well as date
			});
		} else {
			Calendar.setup({
				range: [dateMin, dateMax],
				inputField : name, // ID of the input field
				ifFormat : "%d-%m-%Y", // the date format
				button : "calendar" + name, // ID of the button
				cache : false,
				showsTime : false
			// show time as well as date
			});
		}

		createSpanError(jQuery(calendarImg).next().get(0), name);

		if (this.visibility == 'true') {
			createVisibility(name, calendarImg);
		}

		this.idx++;
	}

	this.remove = function(deleteId, buttonImg) {
		var name = this.propertyPath + '[' + deleteId + ']';
		var inputValue = document.getElementById(name);
		var calendarImg = document.getElementById('calendar' + name);

		if (this.visibility == 'true') {
			var checkbox = 'check' + name + '.visibility';
			var checkboxValue = document.getElementById(checkbox);
			removeElement(checkboxValue);
		}

		removeElement(inputValue);
		removeElement(calendarImg);
		removeElement(jQuery(buttonImg).next().next().get(0)); // br
		removeElement(buttonImg);
		deleteSpanError(name);
	}
}

function DynaClassificazioneInput(contextRoot, jsvar, propertyPath, startIdx,
		cssClass, treename, options, values) {
	this.contextRoot = contextRoot;
	this.jsvar = jsvar;
	this.idx = startIdx;
	this.options = options;
	this.values = values;
	this.cssClass = cssClass;
	this.propertyPath = propertyPath;
	this.treename = treename;

	this.create = function() {
		var name = this.propertyPath + '[' + this.idx + ']';
		var afterElement = jQuery(
				document.getElementById('error' + this.propertyPath + '['
						+ (this.idx - 1) + ']')).prev().get(0);
		var removeImg = createRemoveImgAfter(afterElement, this.jsvar,
				this.contextRoot + '/image/jdyna/delete_icon.gif', this.idx - 1);
		var newLine = createChildElementAfter(jQuery(removeImg).next().get(0),
				'br');
		var inputValue = document.createElement('input');
		var displaySpan = document.createElement('span');
		var selectElement = document.createElement('select');
		var buttonElement = document.createElement('input');

		if (this.idx == 0) { // rimuovo l'img delete per il primo elemento
			removeImg.parentNode.removeChild(removeImg);
		}

		displaySpan.setAttribute('id', 'display' + name);
		jQuery(displaySpan).addClass(cssClass);

		inputValue.setAttribute('id', name);
		inputValue.setAttribute('name', name);
		inputValue.setAttribute('type', 'hidden');

		for ( var i = 0; i < options.length; i++) {
			var option = createChildElement(selectElement, 'option');
			option.setAttribute('value', this.options[i]);
			option.innerHTML = this.values[i];
		}

		selectElement.setAttribute('id', 'classificazione' + name);
		selectElement.setAttribute('name', 'classificazione' + name);
		selectElement.setAttribute('onchange',
				'DWRUtil.useLoadingMessage(); makeSelection(\'' + name
						+ '\',\'display' + name + '\',\'classificazione' + name
						+ '\',\'' + this.treename + '\',this.selectedIndex)');

		selectElement.setAttribute('size', '1');

		buttonElement.setAttribute('id', 'button' + name);
		buttonElement.setAttribute('name', 'button');
		buttonElement.setAttribute('type', 'button');
		buttonElement.setAttribute('onclick', 'reloadSelect(\'' + name
				+ '\',\'display' + name + '\',\'classificazione' + name
				+ '\',\'' + this.treename + '\')');
		buttonElement.setAttribute('value', 'X');

		jQuery(inputValue).insertAfter(jQuery(newLine));
		jQuery(displaySpan).insertAfter(jQuery(inputValue));
		jQuery(selectElement).insertAfter(jQuery(displaySpan));
		jQuery(buttonElement).insertAfter(jQuery(selectElement));

		createSpanError(jQuery(buttonElement).next().get(0), name);
		this.idx++;
	}

	this.remove = function(deleteId, buttonImg) {
		var name = this.propertyPath + '[' + deleteId + ']';
		var inputValue = document.getElementById(name);
		var selectElement = document.getElementById('classificazione' + name);
		var buttonElement = document.getElementById('button' + name);
		var displayElement = document.getElementById('display' + name);
		removeElement(inputValue);
		removeElement(selectElement);
		removeElement(buttonElement);
		removeElement(displayElement);
		removeElement(jQuery(buttonImg).next().next().get(0)); // br
		removeElement(buttonImg);
		deleteSpanError(name);
	}
}

function DynaPointerInput(contextRoot, jsvar, filtro, modelClass, displayExp,
		propertyPath, startIdx, cssClass) {
	this.contextRoot = contextRoot;
	this.jsvar = jsvar;
	this.idx = startIdx;
	this.cssClass = cssClass;
	this.propertyPath = propertyPath;
	this.filtro = filtro;
	this.modelClass = modelClass;
	this.displayExp = displayExp;

	this.create = function() {
		var name = this.propertyPath + '[' + this.idx + ']';
		var suggestbox = 'suggestbox' + name;
		var afterElement = document.getElementById(
				'error' + this.propertyPath + '[' + (this.idx - 1) + ']')
				.previous();

		var removeImg = createRemoveImgAfter(afterElement, this.jsvar,
				this.contextRoot + '/image/jdyna/delete_icon.gif', this.idx - 1);
		var newLine = createChildElementAfter(jQuery(removeImg).next().get(0),
				'br');
		var inputValue = createChildElementAfter(newLine, 'input');
		var suggestElement = createChildElementAfter(inputValue, 'input');
		// var suggestBoxDiv = createChildElementAfter(suggestElement,'div');
		var displayElement = createChildElementAfter(suggestElement, 'input');
		var buttonElement = createChildElementAfter(displayElement, 'input');
		var indicatorSpan = createChildElementAfter(buttonElement, 'span');
		var messageSpan = createChildElementAfter(indicatorSpan, 'span');
		if (this.idx == 0) { // rimuovo l'img delete per il primo elemento
			removeImg.parentNode.removeChild(removeImg);
		}
		inputValue.setAttribute('id', name);
		inputValue.setAttribute('name', name);
		inputValue.setAttribute('type', 'hidden');

		suggestElement.setAttribute('id', suggestbox);
		suggestElement.setAttribute('name', suggestbox);
		suggestElement.setAttribute('onchange',
				'disabilitaTextBox(\'suggestbox' + name + '\');');
		suggestElement.setAttribute('type', 'text');

		displayElement.setAttribute('id', 'display' + name);
		displayElement.setAttribute('name', 'display' + name);
		displayElement.setAttribute('type', 'hidden');
		displayElement.setAttribute('value', this.displayExp);

		indicatorSpan.setAttribute('id', 'indicator' + name);
		indicatorSpan.setAttribute('style', 'display: none;');
		var indicatorImg = createChildElement(indicatorSpan, 'img');
		indicatorImg.setAttribute('src', this.contextRoot
				+ '/image/jdyna/indicator.gif');
		messageSpan.setAttribute('id', 'message' + name);

		buttonElement.setAttribute('id', 'button' + name);
		buttonElement.setAttribute('title', 'Azzera');
		buttonElement.setAttribute('type', 'button');
		buttonElement.setAttribute('onclick', 'document.getElementById(\''
				+ suggestbox
				+ '\').readOnly = false;document.getElementById(\'' + name
				+ '\').value=\'\';document.getElementById(\'' + suggestbox
				+ '\').value=\'\';document.getElementById(\'' + suggestbox
				+ '\').className = \'\';');
		buttonElement.setAttribute('value', 'X');

		createSpanError(jQuery(messageSpan).next().get(0), name);

		new AjaxJspTag.Autocomplete(this.contextRoot
				+ "/ajaxFrontPuntatore.htm", {
			indicator : "indicator" + name,
			source : "suggestbox" + name,
			target : name,
			minimumCharacters : "3",
			className : "autocomplete",
			parameters : "filtro=" + this.filtro + ",query={suggestbox" + name
					+ "},model=" + this.modelClass + ",display={display" + name
					+ "}",
			parser : new ResponseXmlToHtmlListParser()
		});
		this.idx++;
	}

	this.remove = function(deleteId, buttonImg) {
		var name = this.propertyPath + '[' + deleteId + ']';
		var inputValue = document.getElementById(name);
		var suggestElement = document.getElementById('suggestbox' + name);
		var displayElement = document.getElementById('display' + name);
		var indicatorSpan = document.getElementById('indicator' + name);
		var messageSpan = document.getElementById('message' + name);
		var buttonElement = document.getElementById('button' + name);

		removeElement(inputValue);
		removeElement(suggestElement);
		removeElement(buttonElement);
		removeElement(displayElement);
		removeElement(indicatorSpan);
		removeElement(messageSpan);
		removeElement(jQuery(buttonImg).next().next().get(0)); // br
		removeElement(buttonImg);
		deleteSpanError(name);
	}
}

function DynaComboInput(contextRoot, jsvar, propertyPath, startIdx, inline,
		shortnames, labels, repetables, labelsSize, types, newlines) {
	this.contextRoot = contextRoot;
	this.jsvar = jsvar;
	this.idx = startIdx;
	this.shortnames = shortnames;
	this.labels = labels;
	this.repetables = repetables;
	this.propertyPath = propertyPath;
	this.labelsSize = labelsSize;
	this.types = types;
	this.subDynaInput = new Array();
	this.inline = inline;
	this.newlines = newlines;

	this.wait = function(buttonAdd) {
		buttonAdd.setAttribute('src', this.contextRoot
				+ '/image/jdyna/indicator.gif');
	}

	this.create = function(buttonAdd) {
		var previousLine;
		var newline;
		if (this.inline == true) {
			previousLine = buttonAdd.parentNode.parentNode;
			newline = createChildElementAfter(previousLine, 'tr');
		} else {
			previousLine = buttonAdd;
			var subdynaclear = createChildElementAfter(buttonAdd, 'div');
			jQuery(subdynaclear).addClass('dynaClear');
			subdynaclear.innerHTML = '&nbsp;';
			newline = createChildElementAfter(subdynaclear, 'div');
			jQuery(newline).addClass('dynaFieldComboValue');
		}

		this.subDynaInput[this.idx] = new Array();
		for ( var i = 0; i < this.shortnames.length; i++) {
			var subPropertyPath = this.propertyPath + '[' + this.idx
					+ '].object.anagraficaProperties[' + this.shortnames[i]
					+ ']';
			var subDynaJs = jsvar + '.subDynaInput[' + this.idx + '][' + i
					+ ']';

			var tmpTD;
			if (this.inline == true) {
				tmpTD = createChildElement(newline, 'td');
			} else {
				var subdynafield = createChildElement(newline, 'div');
				jQuery(subdynafield).addClass('dynaField');
				// newline.appendChild(subdynafield);
				var label = createChildElement(subdynafield, 'span');
				jQuery(label).addClass('dynaLabel');
				jQuery(label).css('width', this.labelsSize[i] + 'em;');
				label.innerHTML = this.labels[i];
				tmpTD = createChildElementAfter(label, 'div');
				jQuery(tmpTD).addClass('dynaFieldValue');
			}

			var addImgButton = createChildElement(tmpTD, 'img');
			addImgButton.setAttribute('src', this.contextRoot
					+ '/image/jdyna/main_plus.gif');
			jQuery(addImgButton).addClass('addButton');
			addImgButton.setAttribute('onclick', subDynaJs + '.create()');

			var tmpErrorSpan = createChildElementAfter(addImgButton, 'span');
			tmpErrorSpan.setAttribute('id', 'error' + subPropertyPath + '[-1]');
			// tmpTD.appendChild(tmpErrorSpan);

			this.internalCreate(subPropertyPath, subDynaJs, i);
			// rimuovo lo span fittizio
			tmpTD.removeChild(tmpErrorSpan);

			if (this.repetables[i] == false) {
				tmpTD.removeChild(addImgButton);
			}
			// newline.appendChild(tmpTD);

			// rimuovo il primo br
			var tmpBrDelete = tmpTD.firstChild;
			tmpTD.removeChild(tmpBrDelete);

			if ((this.inline == false) && (this.newlines[i] == true)) {
				var subnewline = createChildElementAfter(tmpTD.parentNode,
						'div');
				jQuery(subnewline).addClass('dynaClear');
				subnewline.innerHTML = '&nbsp;';
			}
		}

		// sposto l'img di add a livello di riga
		var globalAddButton = document.getElementById('addButton'
				+ this.propertyPath);
		var previousRowDeleteImg = createChildElementAfter(globalAddButton,
				'img');
		jQuery(previousRowDeleteImg).addClass('deleteButton');
		jQuery(previousRowDeleteImg).bind(
				'click',
				{
					iddelete : (this.idx - 1),
					jsobj : this.jsvar
				},
				function(event) {
					eval(event.data.jsobj + '.remove(' + event.data.iddelete
							+ ', this)');
				});
		previousRowDeleteImg.setAttribute('src', this.contextRoot
				+ '/image/jdyna/delete_icon.gif');

		if (this.inline == true) {
			globalAddButton.parentNode.removeChild(globalAddButton);
			var tmpTD = createChildElement(newline, 'td');
			tmpTD.appendChild(globalAddButton);
			newline.appendChild(tmpTD);
		} else {
			moveAfter(newline, globalAddButton);
		}
		// globalAddButton.setAttribute('src',this.contextRoot+'/images/main_plus.gif');
		this.idx++;
	}

	this.remove = function(deleteId, buttonImg) {
		if (this.inline == true) {
			var rowToDelete = buttonImg.parentNode.parentNode;
			var table = rowToDelete.parentNode.parentNode;
			rowToDelete.parentNode.removeChild(rowToDelete);

			var resetInput = document.createElement('input');
			resetInput.setAttribute('name', '_' + this.propertyPath + '['
					+ deleteId + ']');
			resetInput.setAttribute('value', 'true');
			resetInput.setAttribute('type', 'hidden');
			jQuery(resetInput).insertAfter(jQuery(table));
		} else {
			var divToDelete = jQuery(buttonImg).prev().get(0);
			var divClear = jQuery(buttonImg).next().get(0);
			var resetInput = document.createElement('input');
			resetInput.setAttribute('name', '_' + this.propertyPath + '['
					+ deleteId + ']');
			resetInput.setAttribute('value', 'true');
			resetInput.setAttribute('type', 'hidden');
			jQuery(resetInput).insertAfter(jQuery(buttonImg));
			removeElement(divToDelete);
			removeElement(buttonImg);
			removeElement(divClear);
		}
	}

	this.internalCreate = function(subPropertyPath, subDynaJs, i) {

		if (this.types[i][0] == 'puntatore') {
			this.subDynaInput[this.idx][i] = new DynaPointerInput(
					this.contextRoot, subDynaJs, this.types[i][9],
					this.types[i][2], this.types[i][3], subPropertyPath, 0, '');

			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][0] == 'alberoClassificatorio') {
			this.subDynaInput[this.idx][i] = new DynaClassificazioneInput(
					this.contextRoot, subDynaJs, subPropertyPath, 0, '',
					this.types[i][1], this.types[i][2], this.types[i][3]);

			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][0] == 'testo') {
			if (this.types[i][3]) { // multilinea
				this.subDynaInput[this.idx][i] = new DynaTextAreaInput(
						this.contextRoot, subDynaJs, subPropertyPath, 0,
						this.types[i][1], this.types[i][2], this.types[i][4]);
			} else {
				this.subDynaInput[this.idx][i] = new AddTextInput(
						this.contextRoot, subDynaJs, subPropertyPath, 0, '',
						this.types[i][1], '');
			}
			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][0] == 'numero') {
			this.subDynaInput[this.idx][i] = new AddTextInput(this.contextRoot,
					subDynaJs, subPropertyPath, 0, '', this.types[i][1],
					'number');
			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][0] == 'calendar') {
			this.subDynaInput[this.idx][i] = new DynaDateInput(
					this.contextRoot, subDynaJs, subPropertyPath, 0,
					this.types[i][1]);
			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][1] == 'link') {
			this.subDynaInput[this.idx][i] = new AddLinkInputWithVisibility(
					this.contextRoot, subDynaJs, subPropertyPath, 0, '',
					this.types[i][1], this.types[i][2], this.types[i][3],
					this.types[i][4], '', 'false');
			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][1] == 'file') {
			this.subDynaInput[this.idx][i] = new AddFileInputWithVisibility(
					this.contextRoot, subDynaJs, subPropertyPath, 0, '',
					this.types[i][1], this.types[i][2], '', 'false',
					this.types[i][3], this.types[i][4], this.types[i][5],
					this.types[i][6], this.types[i][7]);
			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][0] == 'boolean') {

			var booleanname = subPropertyPath + '[0]';
			var booleancheckname = 'check' + booleanname;

			var afterElement = document.getElementById(
					'error' + subPropertyPath + '[-1]').previous();
			var removeImg = createRemoveImgAfter(afterElement, this.jsvar,
					this.contextRoot + '/image/jdyna/delete_icon.gif', 0);
			var newLine = createChildElementAfter(jQuery(removeImg).next().get(
					0), 'br');
			var inputValue = document.createElement('input');
			var checkElement = document.createElement('input');

			removeElement(removeImg);
			inputValue.setAttribute('id', booleanname);
			inputValue.setAttribute('name', booleanname);
			inputValue.setAttribute('type', 'hidden');
			inputValue.setAttribute('value', 'false');
			jQuery(inputValue).insertAfter(jQuery(newLine));

			checkElement.setAttribute('id', booleancheckname);
			checkElement.setAttribute('name', booleancheckname);
			checkElement.setAttribute('type', 'checkbox');
			var onchangeJS = '';
			if (this.types[i][1] != '') {
				onchangeJS = this.types[i][1] + '(\'' + booleanname + '\'';
				if (this.types[i][2] != '') {
					onchangeJS += ',' + this.types[i][2];
				}
				onchangeJS += ')';
				if (this.types[i][3] != '') {
					onchangeJS += ',' + this.types[i][3];
				}
			}
			checkElement.setAttribute('onchange', 'cambiaBoolean(\''
					+ booleanname + '\');' + onchangeJS);
			checkElement.setAttribute('value', 'false');
			jQuery(checkElement).insertAfter(jQuery(inputValue));

			createSpanError(jQuery(checkElement).next().get(0), booleanname);
		} else {
			moveAfter(tmpErrorSpan, addImgButton);
			var tmpXX = createChildElementAfter(tmpErrorSpan, 'span');
			tmpXX.innerHTML = this.shortnames[i] + ' - ' + this.types[i];
			var tmpXX = createChildElementAfter(tmpErrorSpan, 'span');
			tmpXX.innerHTML = this.shortnames[i] + ' - ' + this.types[i];
			var tmpXX = createChildElementAfter(tmpErrorSpan, 'span');
			tmpXX.innerHTML = this.shortnames[i] + ' - ' + this.types[i];
		}

	}
}

function DynaComboInputWithVisibility(contextRoot, jsvar, propertyPath,
		startIdx, inline, shortnames, labels, repetables, labelsSize, types,
		newlines, visibility) {
	this.contextRoot = contextRoot;
	this.jsvar = jsvar;
	this.idx = startIdx;
	this.shortnames = shortnames;
	this.labels = labels;
	this.repetables = repetables;
	this.propertyPath = propertyPath;
	this.labelsSize = labelsSize;
	this.types = types;
	this.subDynaInput = new Array();
	this.inline = inline;
	this.newlines = newlines;
	this.visibility = visibility;

	this.wait = function(buttonAdd) {
		buttonAdd.setAttribute('src', this.contextRoot
				+ '/image/jdyna/indicator.gif');
	}

	this.create = function(buttonAdd) {
		var previousLine;
		var newline;
		if (this.inline == true) {
			previousLine = buttonAdd.parentNode.parentNode;
			newline = createChildElementAfter(previousLine, 'tr');
		} else {
			previousLine = buttonAdd;
			var subdynaclear = createChildElementAfter(buttonAdd, 'div');
			jQuery(subdynaclear).addClass('dynaClear');
			subdynaclear.innerHTML = '&nbsp;';
			newline = createChildElementAfter(subdynaclear, 'div');
			jQuery(newline).addClass('dynaFieldComboValue');
		}

		this.subDynaInput[this.idx] = new Array();
		for ( var i = 0; i < this.shortnames.length; i++) {
			var subPropertyPath = this.propertyPath + '[' + this.idx
					+ '].object.anagraficaProperties[' + this.shortnames[i]
					+ ']';
			var subDynaJs = jsvar + '.subDynaInput[' + this.idx + '][' + i
					+ ']';

			var tmpTD;
			if (this.inline == true) {
				tmpTD = createChildElement(newline, 'td');
			} else {
				var subdynafield = createChildElement(newline, 'div');
				jQuery(subdynafield).addClass('dynaField');
				// newline.appendChild(subdynafield);
				var label = createChildElement(subdynafield, 'span');
				jQuery(label).addClass('dynaLabel');
				jQuery(label).css('width', this.labelsSize[i] + 'em');
				label.innerHTML = this.labels[i] + ':';
				tmpTD = createChildElementAfter(label, 'div');
				jQuery(tmpTD).addClass('dynaFieldValue');
			}

			var addImgButton = createChildElement(tmpTD, 'img');
			addImgButton.setAttribute('id', subDynaJs + '_addbutton');
			addImgButton.setAttribute('src', this.contextRoot
					+ '/image/jdyna/main_plus.gif');
			jQuery(addImgButton).addClass('addButton');
			jQuery(addImgButton).click(function() {
				eval(this.id.replace('_addbutton', '') + '.create()');
			});
			var tmpErrorSpan = createChildElementAfter(addImgButton, 'span');
			tmpErrorSpan.setAttribute('id', 'error' + subPropertyPath + '[-1]');
			// tmpTD.appendChild(tmpErrorSpan);

			this.internalCreate(subPropertyPath, subDynaJs, i);
			// rimuovo lo span fittizio
			jQuery(tmpErrorSpan).remove();

			if (this.repetables[i] == false) {
				removeElement(addImgButton);
			}
			// newline.appendChild(tmpTD);

			// rimuovo il primo br
			var tmpBrDelete = tmpTD.firstChild;
			jQuery(tmpBrDelete).remove();

			if ((this.inline == false) && (this.newlines[i] == true)) {
				var subnewline = createChildElementAfter(tmpTD.parentNode,
						'div');
				jQuery(subnewline).addClass('dynaClear');
				subnewline.innerHTML = '&nbsp;';
			}
		}

		// sposto l'img di add a livello di riga
		var globalAddButton = document.getElementById('addButton'
				+ this.propertyPath);
		var previousRowDeleteImg = createChildElementAfter(globalAddButton,
				'img');
		jQuery(previousRowDeleteImg).addClass('deleteButton');
		jQuery(previousRowDeleteImg).bind(
				'click',
				{
					iddelete : (this.idx - 1),
					jsobj : this.jsvar
				},
				function(event) {
					eval(event.data.jsobj + '.remove(' + event.data.iddelete
							+ ', this)');
				});
		previousRowDeleteImg.setAttribute('src', this.contextRoot
				+ '/image/jdyna/delete_icon.gif');

		if (this.inline == true) {
			globalAddButton.parentNode.removeChild(globalAddButton);
			var tmpTD = createChildElement(newline, 'td');
			tmpTD.appendChild(globalAddButton);
			newline.appendChild(tmpTD);
		} else {
			moveAfter(newline, globalAddButton);
		}
		// globalAddButton.setAttribute('src',this.contextRoot+'/images/main_plus.gif');
		this.idx++;
	}

	this.remove = function(deleteId, buttonImg) {
		if (this.inline == true) {
			var rowToDelete = buttonImg.parentNode.parentNode;
			var table = rowToDelete.parentNode.parentNode;
			rowToDelete.parentNode.removeChild(rowToDelete);

			var resetInput = document.createElement('input');
			resetInput.setAttribute('name', '_' + this.propertyPath + '['
					+ deleteId + ']');
			resetInput.setAttribute('value', 'true');
			resetInput.setAttribute('type', 'hidden');
			jQuery(resetInput).insertAfter(jQuery(table));
		} else {
			var divToDelete = jQuery(buttonImg).prev().get(0);
			var divClear = jQuery(buttonImg).next().get(0);
			var resetInput = document.createElement('input');
			resetInput.setAttribute('name', '_' + this.propertyPath + '['
					+ deleteId + ']');
			resetInput.setAttribute('value', 'true');
			resetInput.setAttribute('type', 'hidden');
			jQuery(resetInput).insertAfter(jQuery(buttonImg));
			removeElement(divToDelete);
			removeElement(buttonImg);
			removeElement(divClear);
		}
	}

	this.internalCreate = function(subPropertyPath, subDynaJs, i, addButton) {

		if (this.types[i][0] == 'puntatore') {
			this.subDynaInput[this.idx][i] = new DynaPointerInput(
					this.contextRoot, subDynaJs, this.types[i][9],
					this.types[i][2], this.types[i][3], subPropertyPath, 0, '');

			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][0] == 'alberoClassificatorio') {
			this.subDynaInput[this.idx][i] = new DynaClassificazioneInput(
					this.contextRoot, subDynaJs, subPropertyPath, 0, '',
					this.types[i][1], this.types[i][2], this.types[i][3]);

			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][0] == 'testo') {
			if (this.types[i][3]) { // multilinea
				this.subDynaInput[this.idx][i] = new DynaTextAreaInput(
						this.contextRoot, subDynaJs, subPropertyPath, 0,
						this.types[i][1], this.types[i][2], this.types[i][4]);
			} else {
				this.subDynaInput[this.idx][i] = new AddTextInputWithVisibility(
						this.contextRoot, subDynaJs, subPropertyPath, 0, '',
						this.types[i][1], '', 'true');
			}
			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][0] == 'numero') {
			this.subDynaInput[this.idx][i] = new AddTextInput(this.contextRoot,
					subDynaJs, subPropertyPath, 0, '', this.types[i][1],
					'number');
			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][0] == 'calendar') {
			this.subDynaInput[this.idx][i] = new DynaDateInputWithVisibility(
					this.contextRoot, subDynaJs, subPropertyPath, 0,
					this.types[i][1], 'true');
			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][0] == 'link') {
			this.subDynaInput[this.idx][i] = new AddLinkInputWithVisibility(
					this.contextRoot, subDynaJs, subPropertyPath, 0, '',
					this.types[i][1], this.types[i][2], this.types[i][3],
					this.types[i][4], '', 'true');
			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][0] == 'file') {
			this.subDynaInput[this.idx][i] = new AddFileInputWithVisibility(
					this.contextRoot, subDynaJs, subPropertyPath, 0, '',
					this.types[i][1], this.types[i][2], '', 'true',
					this.types[i][3], this.types[i][4], this.types[i][5],
					this.types[i][6]);
			this.subDynaInput[this.idx][i].create();
		} else if (this.types[i][0] == 'boolean') {

			var booleanname = subPropertyPath + '[0]';
			var booleancheckname = 'check' + booleanname;

			var afterElement = document.getElementById(
					'error' + subPropertyPath + '[-1]').previous();
			var removeImg = createRemoveImgAfter(afterElement, this.jsvar,
					this.contextRoot + '/image/jdyna/delete_icon.gif', 0);
			var newLine = createChildElementAfter(jQuery(removeImg).next().get(
					0), 'br');
			var inputValue = document.createElement('input');
			var checkElement = document.createElement('input');

			removeElement(removeImg);
			inputValue.setAttribute('id', booleanname);
			inputValue.setAttribute('name', booleanname);
			inputValue.setAttribute('type', 'hidden');
			inputValue.setAttribute('value', 'false');
			jQuery(inputValue).insertAfter(jQuery(newLine));

			checkElement.setAttribute('id', booleancheckname);
			checkElement.setAttribute('name', booleancheckname);
			checkElement.setAttribute('type', 'checkbox');
			var onchangeJS = '';
			if (this.types[i][1] != '') {
				onchangeJS = this.types[i][1] + '(\'' + booleanname + '\'';
				if (this.types[i][2] != '') {
					onchangeJS += ',' + this.types[i][2];
				}
				onchangeJS += ')';
				if (this.types[i][3] != '') {
					onchangeJS += ',' + this.types[i][3];
				}
			}
			checkElement.setAttribute('onchange', 'cambiaBoolean(\''
					+ booleanname + '\');' + onchangeJS);
			checkElement.setAttribute('value', 'false');
			jQuery(checkElement).insertAfter(jQuery(inputValue));
			createSpanError(jQuery(checkElement).next().get(0), booleanname);
		} else {
			moveAfter(tmpErrorSpan, addImgButton);
			var tmpXX = createChildElementAfter(tmpErrorSpan, 'span');
			tmpXX.innerHTML = this.shortnames[i] + ' - ' + this.types[i];
			var tmpXX = createChildElementAfter(tmpErrorSpan, 'span');
			tmpXX.innerHTML = this.shortnames[i] + ' - ' + this.types[i];
			var tmpXX = createChildElementAfter(tmpErrorSpan, 'span');
			tmpXX.innerHTML = this.shortnames[i] + ' - ' + this.types[i];
		}

	}
}

function callSearchCollision(fieldSearch, collisionClass, inputName, root) {
	document.getElementById('collision' + inputName).innerHTML = '';
	var value = document.getElementById(inputName).value;

	var divCollisione = document.getElementById('collision' + inputName);
	AjaxService
			.searchCollisioni(
					fieldSearch,
					collisionClass,
					value,
					root,
					function(options) {

						document.getElementById('collision' + inputName).innerHTML = '';
						// var dettaglio = root+"/details.htm?id="+options[1];
						var dettaglio = options[1];

						if (options[2] > 0) {
							var immagineWarning = createChildElement(
									divCollisione, 'img');
							immagineWarning.setAttribute('alt', 'Attenzione');
							immagineWarning.setAttribute('src', root
									+ '/image/jdyna/iconWarning.gif');
							immagineWarning
									.setAttribute('title',
											'Attenzione trovati oggetti sul db con metadato simile');
							jQuery(immagineWarning).addClass('warn');
							var messageWarning = createChildElement(
									divCollisione, 'span');
							messageWarning.innerHTML = 'Forse volevi modificare: '

							var linkDettaglio = createChildElement(
									messageWarning, 'a');
							linkDettaglio.setAttribute('href', dettaglio);
							linkDettaglio.setAttribute('target', '_blank');
							linkDettaglio.innerHTML = options[0];
						}
						if (options[2] > 1) {

							var position = collisionClass.lastIndexOf(".");
							var nameClasse = collisionClass
									.substr(position + 1);
							var classe = nameClasse.toLowerCase();

							createChildElement(divCollisione, 'br');

							var linkAllDettaglio = createChildElement(
									divCollisione, 'a');
							linkAllDettaglio
									.setAttribute(
											'href',
											root
													+ "/flusso.flow?_flowId=ricercaSemplice-flow&classe="
													+ classe + "&query="
													+ fieldSearch + ":("
													+ options[3] + ")");
							linkAllDettaglio.setAttribute('target', '_blank');

							var messageWarningContinuazione = createChildElement(
									linkAllDettaglio, 'span');
							messageWarningContinuazione.innerHTML = 'Vedi le '
									+ options[2] + ' possibili corrispondenze';
						}
					});

}

function AddLinkInputWithVisibility(contextRoot, jsvar, propertyPath, startIdx,
		onchange, size, repetable, labelSx, labelDx, cssClass, visibility) {
	this.contextRoot = contextRoot;
	this.jsvar = jsvar;
	this.idx = startIdx;
	this.size = size;
	this.repetable = repetable;
	this.labelSx = labelSx;
	this.labelDx = labelDx;
	this.propertyPath = propertyPath;
	this.visibility = visibility;

	this.create = function() {
		var newline;
		var buttonAdd = document.getElementById(this.jsvar + '_addbutton');
		if (this.idx == 0) {
			var errorspan = 'error' + this.propertyPath + '[-1]';
			var errElem = jQuery(document.getElementById(errorspan)).get(0);
			var firstbr = createChildElementAfter(errElem, 'br');
			var table = createChildElementAfter(firstbr, 'table');
			if (this.labelSx != '' || this.labelDx != '') {
				var thead = createChildElement(table, 'thead');
				var thr = createChildElement(thead, 'tr');
				var thsx = createChildElement(thr, 'th');
				var thdx = createChildElement(thr, 'th');
				createChildElement(thr, 'th');
				if (labelSx != null) {
					thsx.innerHTML = labelSx;
				} else {
					thsx.innerHTML = '&nbsp;';
				}
				if (labelDx != null) {
					thdx.innerHTML = labelDx;
				} else {
					thdx.innerHTML = '&nbsp;';
				}
			}

			newline = createChildElement(table, 'tr');
		} else {
			var previousLine = buttonAdd.parentNode.parentNode;
			newline = createChildElementAfter(previousLine, 'tr');
		}
		var tmpTDTitle = createChildElement(newline, 'td');
		var tmpTDLink = createChildElement(newline, 'td');
		var tmpTDOption = createChildElement(newline, 'td');

		var name = this.propertyPath + '[' + this.idx + ']';

		var inputHidden = document.createElement('input');
		inputHidden.setAttribute('id', name);
		inputHidden.setAttribute('name', name);
		inputHidden.setAttribute('value', '');
		inputHidden.setAttribute('type', 'hidden');
		jQuery(inputHidden).appendTo(jQuery(tmpTDTitle));

		var inputDescription = createChildElement(tmpTDTitle, 'input');
		var inputValue = createChildElement(tmpTDLink, 'input');

		linkdescription = 'linkdescription' + name;
		linkvalue = 'linkvalue' + name;

		inputDescription.setAttribute('id', linkdescription);
		inputDescription.setAttribute('name', linkdescription);
		inputDescription.setAttribute('size', size);
		jQuery(inputDescription).bind(
				'change',
				{
					n : name
				},
				function(event) {
					dynaLinkValueUpdate(event.data.n, 'linkvalue'
							+ event.data.n, 'linkdescription' + event.data.n);
				});

		inputValue.setAttribute('id', linkvalue);
		inputValue.setAttribute('name', linkvalue);
		inputValue.setAttribute('size', size);
		jQuery(inputValue).bind(
				'change',
				{
					n : name
				},
				function(event) {
					dynaLinkValueUpdate(event.data.n, 'linkvalue'
							+ event.data.n, 'linkdescription' + event.data.n);
				});
		inputValue.setAttribute('size', this.size);
		if (cssClass != '') {
			jQuery(inputValue).addClass(cssClass);
		}

		createSpanError(inputValue, name);
		var tmpbr = createChildElement(tmpTDOption, 'br');
		var viselem;
		if (this.visibility == 'true') {
			viselem = createVisibility(name, tmpbr);
		}
		jQuery(tmpbr).remove();

		// sposto l'img di add a livello di riga
		if (this.repetable) {
			if (this.idx > 0) {
				var previousRowDeleteImg = createChildElementAfter(buttonAdd,
						'img');
				jQuery(previousRowDeleteImg).addClass('deleteButton');
				jQuery(previousRowDeleteImg).bind(
						'click',
						{
							iddelete : (this.idx - 1),
							jsobj : this.jsvar
						},
						function(event) {
							eval(event.data.jsobj + '.remove('
									+ event.data.iddelete + ', this)');
						});
				previousRowDeleteImg.setAttribute('src', this.contextRoot
						+ '/image/jdyna/delete_icon.gif');
			}

			jQuery(buttonAdd).appendTo(jQuery(tmpTDOption));
		}
		this.idx++;
	}

	this.remove = function(deleteId, buttonImg) {
		var removeholder = document.getElementById('_' + this.propertyPath
				+ '[' + deleteId + ']');
		var myLine = buttonImg.parentNode.parentNode;
		if (removeholder != null) {
			jQuery(removeholder).insertAfter(jQuery(myLine));
		}

		removeElement(myLine);
	}
}

var timerSearchCollision = null;
var keyPressed = 3;
// ritarda la partenza della funzione javascript che cerca le collisioni
// (timeout ad 1 secondo e keyPressed a 3)
function searchCollision(fieldSearch, collisionClass, inputName, root) {
	var value = document.getElementById(inputName).value;
	if (value.length > keyPressed) {
		if (timerSearchCollision) {
			clearTimeout(timerSearchCollision);
		}
		timerSearchCollision = setTimeout("callSearchCollision('" + fieldSearch
				+ "','" + collisionClass + "','" + inputName + "', '" + root
				+ "')", 1000);
	}
}

dragANDdrop = function dragANDdrop() {
	Sortable.create("firstlist", {
		dropOnEmpty : true,
		containment : [ "firstlist", "secondlist" ],
		constraint : false
	});
	Sortable.create("secondlist", {
		dropOnEmpty : true,
		handle : 'handle',
		containment : [ "firstlist", "secondlist" ],
		constraint : false
	});
}

send = function send() {
	var stringa = Sortable.sequence('secondlist');
	var element = document.getElementById('mascheraxxx');
	element.value = stringa;
}

function cambiaBoolean(booleanId) {
	element = document.getElementById(booleanId);
	if (element.value == null || element.value == 'false'
			|| element.value == '')
		element.value = 'true';
	else
		element.value = 'false';
}

function cambiaBooleano(booleanId, valore, count) {
	element = document.getElementsByName(booleanId);
	if (element[count].value == null || element[count].value == 'false'
			|| element[count].value == '')
		element[count].value = valore;
	else
		element[count].removeAttribute("value");
}

function dynaLinkValueUpdate(inputName, inputLinkValue, inputLinkDescription) {
	document.getElementById(inputName).value = document
			.getElementById(inputLinkDescription).value
			+ '|||' + document.getElementById(inputLinkValue).value;
}

function dynaFileValueMarkAsToDeleted(inputName, inputValue,
		inputCheckDeleteName) {
	var check = document.getElementById(inputCheckDeleteName).value;
	check = (check == 'false' ? 'true' : 'false');
	document.getElementById(inputCheckDeleteName).value = check;
	document.getElementById(inputName).value = inputValue + '|||' + check;
}

function AddFileInputWithVisibility(contextRoot, jsvar, propertyPath, startIdx,
		onchange, size, repetable, cssClass, visibility, showPreview,
		fileDescription, servletPath, labelAnchor, folder) {
	this.contextRoot = contextRoot;
	this.jsvar = jsvar;
	this.idx = startIdx;
	this.size = size;
	this.repetable = repetable;
	this.propertyPath = propertyPath;
	this.visibility = visibility;
	this.showPreview = showPreview;
	this.fileDescription = fileDescription;
	this.servletPath = servletPath;
	this.labelAnchor = labelAnchor;
	this.folder = folder;

	this.create = function() {
		var newline;
		var buttonAdd = document.getElementById(this.jsvar + '_addbutton');
		if (this.idx == 0) {
			var errorspan = 'error' + this.propertyPath + '[-1]';
			var errElem = jQuery(document.getElementById(errorspan)).get(0);
			var firstbr = createChildElementAfter(errElem, 'br');
			var table = createChildElementAfter(firstbr, 'table');

			newline = createChildElement(table, 'tr');
		} else {
			var previousLine = buttonAdd.parentNode.parentNode;
			newline = createChildElementAfter(previousLine, 'tr');
		}
		var tmpTDTitle = createChildElement(newline, 'td');
		var tmpTDOption = createChildElement(newline, 'td');

		var name = this.propertyPath + '[' + this.idx + ']';

		var inputValue = document.createElement('input');
		inputValue.setAttribute('id', name);
		inputValue.setAttribute('name', name);
		inputValue.setAttribute('type', 'file');
		jQuery(inputValue).appendTo(jQuery(tmpTDTitle));

		if (cssClass != '') {
			jQuery(inputValue).addClass(cssClass);
		}

		createSpanError(inputValue, name);
		var tmpbr = createChildElement(tmpTDOption, 'br');
		var viselem;
		if (this.visibility == 'true') {
			viselem = createVisibility(name, tmpbr);
		}
		jQuery(tmpbr).remove();

		// sposto l'img di add a livello di riga
		if (this.repetable) {
			if (this.idx > 0) {
				var previousRowDeleteImg = createChildElementAfter(buttonAdd,
						'img');
				jQuery(previousRowDeleteImg).addClass('deleteButton');
				jQuery(previousRowDeleteImg).bind(
						'click',
						{
							iddelete : (this.idx - 1),
							jsobj : this.jsvar
						},
						function(event) {
							eval(event.data.jsobj + '.remove('
									+ event.data.iddelete + ', this)');
						});
				previousRowDeleteImg.setAttribute('src', this.contextRoot
						+ '/image/jdyna/delete_icon.gif');
			}

			jQuery(buttonAdd).appendTo(jQuery(tmpTDOption));
		}
		this.idx++;
	}

	this.remove = function(deleteId, buttonImg) {
		var removeholder = document.getElementById('_' + this.propertyPath
				+ '[' + deleteId + ']');
		var inputValue = document.getElementById(this.propertyPath + '['
				+ deleteId + ']').value;
		document.getElementById(this.propertyPath + '[' + deleteId + ']').value = inputValue
				+ '|||' + "true";
		var myLine = buttonImg.parentNode.parentNode;
		if (removeholder != null) {
			jQuery(removeholder).insertAfter(jQuery(myLine));
		}
		jQuery(myLine).toggle();

	}
}