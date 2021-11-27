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
        return `<td>
            <div class="form-group">
                <select data-m2m-id="${id}" class="m2m-input-${fieldName} form-control">
                    <option disabled selected value> -- select an option -- </option>
                    ${options}
                </select>
            </div>
        </td>`
    }
    if (def.type === 'sequence') {
        return `<td class="fit">
            <div class="form-group">
                <input data-m2m-id="${id}" 
                       disabled width="70" class="m2m-input-${fieldName} form-control m2m-sequence" 
                       type="number" value="${value}"/>
                <a data-m2m-id="${id}" class="m2m-seq-up-btn-${fieldName} btn btn-info btn-circle">
                   <i class="fas fa-arrow-up"></i></a>
                <a data-m2m-id="${id}" class="m2m-seq-down-btn-${fieldName} btn btn-info btn-circle">
                   <i class="fas fa-arrow-down"></i></a>
            </div>
        </td>`
    }

    return `
        <td>
            <div class="form-group">
                <input data-m2m-id="${id}" class="m2m-input-${fieldName} form-control" type="${def.type}" value="${value}"/>
            </div>
        </td>
    `
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


function build(target) {
    const input = $(target).children('input')
    const defs = $(target).children('.m2m-field').map(getFieldDef).get()
    const sourceData = defs.some(d => d.type === 'sequence')
        ? decode(input.val()).sort((a, b) => +a['sequence'] - +b['sequence'])
        : decode(input.val())

    const name = input.attr('name')
    const title = $(target).data('m2m-caption')
    const caption = $(`<h4>${title}</h4>`)
    const table = $(`<table class='table m2m-table table-bordered'></table>`)

    let index = 0;
    const rows = sourceData.map((value) => buildRow(defs, value, name, index++))
    const body = $(`<tbody>${rows}</tbody>`)
    const addBtn = $(`<a class="btn btn-link"><i class="fas fa-plus"/> Add new line</a>`)

    const m2mEvent = new M2mEvent(sourceData, input, name)

    table.append(buildHeadings(defs))
    table.append(body)
    table.append(addBtn)
    $(target).hide().after(table)
    table.before(caption)

    addBtn.click(function () {
        body.append(buildRow(defs, {}, name, index++))
        sourceData.push({})
        m2mEvent.updateEvents()
    })
    m2mEvent.updateEvents()
}

$(document).ready(function () {
    $('.multiple-select').select2();

    $('.m2m').each(function () {
        build(this)
    })
})