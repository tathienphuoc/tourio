/**
 *
 * @param field {Element}
 * @returns {{data: [{value: string, text: string}], label: string, type: string}}
 */
function getFieldData(field) {
    const type = $(field).data('m2m-type')
    const label = $(field).data('m2m-label')
    let data = []
    if (type === 'select') {
        $(field).children('data').each(function () {
            data.push({
                value: $(this).val(),
                text: $(this).text()
            })
        })
    }
    return {type, label, data}
}

/**
 *
 * @param wrapper {Element}
 * @returns [{data: [{value: string, text: string}], label: string, type: string}]
 */
function getFieldDataList(wrapper) {
    return $(wrapper).children('.m2m-field').map(function () {
        return getFieldData(this)
    }).get()
}

/**
 *
 * @param data {[{data: [{value: string, text: string}], label: string, type: string}]}
 * @returns {string}
 */
function buildHeadings(data) {
    const cols = data.map(d => `<th>${d.label}</th>`)
    return `<thead><tr>${cols} <th class="fit"></th></tr></thead>`
}

/**
 *
 * @param val {string}
 * @returns [string[]]
 */
function decode(val) {
    return val !== '' ? val.split(',').map(row => row.split('|')) : []
}

/**
 *
 * @param def {{data: [{value: string, text: string}], label: string, type: string}}
 * @param value {string}
 * @param id {string}
 * @returns {string}
 */
function buildCell(def, value, id) {
    let cell
    if (def.type === 'select') {
        const options = def.data.map(d => `<option ${d.value === value ? 'selected' : ''} value="${d.value}">${d.text}</option>`).join()
        cell = `
            <div class="form-group">
                <select data-m2m-id="${id}" class="m2m-input form-control">
                    <option disabled selected value> -- select an option -- </option>
                    ${options}
                </select>
            </div>
        `
    } else {
        cell = `
            <div class="form-group">
                <input data-m2m-id="${id}" class="m2m-input form-control" type="${def.type}" value="${value}"/>
            </div>
        `
    }
    return `<td>${cell}</td>`
}

/**
 *
 * @param defs {[{data: [{value: string, text: string}], label: string, type: string}]}
 * @param values {[string]}
 * @param id {string}
 */
function buildRow(defs, values, id) {
    let cells = defs.map((d, i) => buildCell(d, values[i] || '', id + '-' + i))
    cells.push(`
        <td class="fit">
            <a data-m2m-id="${id}" class="m2m-remove-btn btn btn-danger btn-circle">
               <i class="fas fa-trash"></i></a>
        </td>
    `)
    return `<tr id="${id}">${cells}</tr>`
}

/**
 *
 * @param data {[string[]]}
 * @return {string}
 */
function encode(data) {
    return data.filter(r => r.length).map(row => row.map(c => c === '' ? 'null' : c).join('|')).join(',')
}

function updateRemoveEvent(sourceData, input) {
    const removeBtn = $('.m2m-remove-btn')
    removeBtn.off('click')
    removeBtn.click(function () {
        const id = $(this).data('m2m-id')
        sourceData[+id.split('-')[1]] = []
        $(`#${id}`).hide()
        input.val(encode(sourceData))
    })
}


function updateEditEvent(sourceData, input) {
    const removeBtn = $('.m2m-input')
    removeBtn.off('input')
    removeBtn.on('input', function () {
        const [, row, col] = $(this).data('m2m-id').split('-')
        sourceData[+row][+col] = $(this).val()
        input.val(encode(sourceData))
    })
}

/**
 *
 * @param target {Element}
 *
 */
function build(target) {
    const input = $(target).children('input')
    const defs = getFieldDataList(target)
    const sourceData = decode(input.val())
    const name = input.attr('name')
    const title = $(target).data('m2m-caption')

    const caption = $(`<h4>${title}</h4>`)
    const table = $(`<table class='table m2m-table table-bordered'></table>`)
    const body = $(`<tbody>${sourceData.map((value, index) => buildRow(defs, value, name + '-' + index))}</tbody>`)
    const addBtn = $(`<a class="btn btn-link"><i class="fas fa-plus"/> Add new line</a>`)

    table.append(buildHeadings(defs))
    table.append(body)
    table.append(addBtn)
    $(target).hide().after(table)
    table.before(caption)

    addBtn.click(function () {
        body.append(buildRow(defs, [], name + '-' + sourceData.length))
        sourceData.push(Array(defs.length).fill(''))
        updateRemoveEvent(sourceData, input)
        updateEditEvent(sourceData, input)
    })

    updateEditEvent(sourceData, input)
    updateRemoveEvent(sourceData, input)
}

$(document).ready(function () {
    $('.m2m').each(function () {
        build(this)
    })
})