import ShoppingListCore
import SwiftUI

struct ContentView: View {
    @State private var listId = ""

    var body: some View {
        NavigationStack {
            VStack {
                TextField("Enter Listname", text: $listId)
                    .disableAutocorrection(true)
                    .textFieldStyle(BackgroundContrastStyle())
                    .padding(.horizontal, 32)
                NavigationLink("Load", destination: ShoppingListPage(listId: listId))
                    .padding(.top, 8)
                    .disabled(listId.isEmpty)
            }
        }
    }
}

struct ContentViewPreview: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
