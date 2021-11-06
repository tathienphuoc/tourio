/**
 * @returns {{label: string, type: string, name: string, data: [{value: string, text: string}]}}
 */
function getFieldDef() {
    const type = $(this).data('m2m-type')
    const label = $(this).data('m2m-label')
    const name = $(this).data('m2m-name')
    let data = []
    if (type === 'select') {
        $(this).children('data').each(function () {
            data.push({
                value: $(this).val(),
                text: $(this).text()
            })
        })
    }
    return {type, label, name, data}
}

/**
 *
 * @param data {[{label: string, type: string, name: string, data: [{value: string, text: string}]}]}
 * @returns {string}
 */
function buildHeadings(data) {
    const cols = data.map(d => `<th>${d.label}</th>`)
    return `<thead><tr>${cols} <th class="fit"></th></tr></thead>`
}

/**
 *
 * @param def {{label: string, type: string, name: string, data: [{value: string, text: string}]}}
 * @param value {string}
 * @param id {string}
 * @returns {string}
 */
function buildCell(def, value, id) {
    let cell
    const [fieldName] = id.split('-')
    if (def.type === 'select') {
        const options = def.data.map(d => `<option ${d.value === value.toString() ? 'selected' : ''} value="${d.value}">${d.text}</option>`).join()
        cell = `
            <div class="form-group">
                <select data-m2m-id="${id}" class="m2m-input-${fieldName} form-control">
                    <option disabled selected value> -- select an option -- </option>
                    ${options}
                </select>
            </div>
        `
    } else {
        cell = `
            <div class="form-group">
                <input data-m2m-id="${id}" class="m2m-input-${fieldName} form-control" type="${def.type}" value="${value}"/>
            </div>
        `
    }
    return `<td>${cell}</td>`
}

/**
 *
 * @param defs {[{label: string, type: string, name: string, data: [{value: string, text: string}]}]}
 * @param value {Object}
 * @param name {string}
 * @param index {number}
 */
function buildRow(defs, value, name, index) {
    const rowId = name + '-' + index
    let cells = defs.map((d) => buildCell(d, value[d.name] || '', rowId + '-' + d.name))
    cells.push(`
        <td class="fit">
            <a data-m2m-id="${rowId}" class="m2m-remove-btn-${name} btn btn-danger btn-circle">
               <i class="fas fa-trash"></i></a>
        </td> 
    `)
    return `<tr id="${rowId}">${cells}</tr>`
}

/**
 *
 * @param val {string}
 * @returns {[Object]}
 */
function decode(val) {
    return val !== '' ? JSON.parse(val) : []
}

/**
 *
 * @param data {[Object]}
 * @return {string}
 */
function encode(data) {
    console.log(JSON.stringify(data))
    return JSON.stringify(data)
}

function updateRemoveEvent(sourceData, input, name) {
    const removeBtn = $(`.m2m-remove-btn-${name}`)
    removeBtn.off('click')
    removeBtn.click(function () {
        const id = $(this).data('m2m-id')
        sourceData[+id.split('-')[1]] = {}
        $(`#${id}`).remove()
        input.val(encode(sourceData))
    })
}

/**
 *
 * @param sourceData {[Object]}
 * @param input
 * @param name {string}
 */
function updateEditEvent(sourceData, input, name) {
    const inputField = $(`.m2m-input-${name}`)
    inputField.off('input')
    inputField.on('input', function () {
        const [, rowId, colName] = $(this).data('m2m-id').split('-')
        sourceData[+rowId][colName] = $(this).val().toString()
        input.val(encode(sourceData))
    })
}

function build(target) {
    const input = $(target).children('input')
    const defs = $(target).children('.m2m-field').map(getFieldDef).get()
    const sourceData = decode(input.val())
    const name = input.attr('name')
    const title = $(target).data('m2m-caption')
    const caption = $(`<h4>${title}</h4>`)
    const table = $(`<table class='table m2m-table table-bordered'></table>`)
    let index = 0;
    const body = $(`<tbody>${sourceData.map((value) => buildRow(defs, value, name, index++))}</tbody>`)
    const addBtn = $(`<a class="btn btn-link"><i class="fas fa-plus"/> Add new line</a>`)

    table.append(buildHeadings(defs))
    table.append(body)
    table.append(addBtn)
    $(target).hide().after(table)
    table.before(caption)

    addBtn.click(function () {
        body.append(buildRow(defs, {}, name, index++))
        sourceData.push({})
        updateRemoveEvent(sourceData, input, name)
        updateEditEvent(sourceData, input, name)
    })

    updateRemoveEvent(sourceData, input, name)
    updateEditEvent(sourceData, input, name)
}

$(document).ready(function () {
    $('.multiple-select').select2();

    $('.m2m').each(function () {
        build(this)
    })
})