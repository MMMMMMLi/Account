const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}

function getArrayIndex(arr, obj) {
  var i = arr.length;
  while (i--) {
      if (arr[i] === obj) {
          return i;
      }
  }
  return -1;
}

module.exports = {
  formatTime: formatTime,
  getArrayIndex:getArrayIndex
}
