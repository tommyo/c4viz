import { Client } from '@stomp/stompjs'

export let connected = false

const stompClient = new Client({
  brokerURL: 'ws://localhost:9000/ws/connect',
  debug: console.log
})

stompClient.onDisconnect = () => {
  connected = false
}

stompClient.onConnect = (frame) => {
  console.log({ frame });
  connected = true

  stompClient.subscribe('/topic/change', (msg) => {
    console.log('/topic/change', msg.body)
  })

  stompClient.subscribe('/topic/error', (error) => {
    console.log({ error })
  })

  fetch('/api/files')
    .then(async (res) => {
      console.log(await res.json())
    })
    .catch((error) => {
      console.log({ error });
    })
}

stompClient.onStompError = (err) => {
  console.log({ err })
  connected = false
}
export function connect() {
  stompClient.activate()
}

export function disconnect() {
  stompClient.deactivate()
}
